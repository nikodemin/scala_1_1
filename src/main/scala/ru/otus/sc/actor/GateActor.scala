package ru.otus.sc.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.Timeout
import ru.otus.sc.actor.CachingActor.Get
import ru.otus.sc.dao.{AlbumDao, BandDao, TrackDao}
import ru.otus.sc.model.dto.{AlbumGetDto, BandGetDto, TrackGetDto}

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success}

object GateActor {

  sealed trait Message

  case class FindByNameContaining(string: String, replyTo: ActorRef[Result]) extends Message

  case class Result(string: String, bands: List[BandGetDto], albums: List[AlbumGetDto],
                    tracks: List[TrackGetDto]) extends Message

  case class NotFoundInCache(string: String) extends Message

  def apply(bandDao: BandDao, albumDao: AlbumDao, trackDao: TrackDao)
           (implicit duration: FiniteDuration, timeout: Timeout, executionContext: ExecutionContext): Behavior[Message] = Behaviors.setup { ctx =>
    val cache = ctx.spawn(CachingActor(), "cache")
    val aggregatorActor = ctx.spawn(AggregatorActor(duration = duration, gateActor = ctx.self, cachingActor = cache), "aggregator")
    val router = ctx.spawn(HandlingActor.pool(bandDao, albumDao, trackDao, aggregatorActor), "router")
    val reqSet = mutable.Set[FindByNameContaining]()

    Behaviors.receiveMessage {

      case req@FindByNameContaining(string, replyTo) =>
        reqSet += req
        ctx.ask(cache, (ref: ActorRef[Message]) => Get(string, ref)) {
          case Success(value) => value
          case Failure(ex) =>
            ctx.log.error("Error retrieving value from cache", ex)
            NotFoundInCache(string)
        }
        Behaviors.same

      case NotFoundInCache(string) =>
        ctx.log.info(s"$string is not found in cache. Searching in db...")

        router ! HandlingActor.GetBandsByNameContaining(string)
        router ! HandlingActor.GetAlbumsByNameContaining(string)
        router ! HandlingActor.GetTracksByNameContaining(string)
        Behaviors.same

      case res@Result(string, bands, albums, tracks) =>
        val req = reqSet.find(_.string == string).get
        req.replyTo ! res
        reqSet -= req
        Behaviors.same
    }
  }
}
