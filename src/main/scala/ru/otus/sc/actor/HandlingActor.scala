package ru.otus.sc.actor

import akka.actor.typed.scaladsl.{Behaviors, PoolRouter, Routers}
import akka.actor.typed.{ActorRef, Behavior}
import ru.otus.sc.dao.{AlbumDao, BandDao, TrackDao}
import ru.otus.sc.model.dto.{AlbumGetDto, BandGetDto, TrackGetDto}

import scala.concurrent.ExecutionContext

object HandlingActor {

  sealed trait Message

  case class GetAlbumsByNameContaining(string: String) extends Message

  case class GetBandsByNameContaining(string: String) extends Message

  case class GetTracksByNameContaining(string: String) extends Message


  def apply(bandDao: BandDao, albumDao: AlbumDao, trackDao: TrackDao, aggregatorActor: ActorRef[AggregatorActor.Message])
           (implicit executionContext: ExecutionContext): Behavior[Message] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage {

      case GetAlbumsByNameContaining(string) =>
        albumDao.getByNameContaining(string).onComplete { listTry =>
          val list = AlbumGetDto.fromTupleList(listTry.getOrElse(List.empty))
          aggregatorActor ! AggregatorActor.Result(string, albums = list)
        }
        Behaviors.same

      case GetBandsByNameContaining(string) =>
        bandDao.getByNameContaining(string).onComplete { listTry =>
          val list = BandGetDto.fromTupleList(listTry.getOrElse(List.empty))
          aggregatorActor ! AggregatorActor.Result(string, bands = list)
        }
        Behaviors.same

      case GetTracksByNameContaining(string) =>
        trackDao.getByNameContaining(string).onComplete { listTry =>
          val list = listTry.getOrElse(List.empty).map(TrackGetDto.fromTrackRow)
          aggregatorActor ! AggregatorActor.Result(string, tracks = list)
        }
        Behaviors.same
    }
  }

  def pool(bandDao: BandDao, albumDao: AlbumDao, trackDao: TrackDao, aggregatorActor: ActorRef[AggregatorActor.Message])
          (implicit executionContext: ExecutionContext): PoolRouter[Message] =
    Routers.pool(3)(HandlingActor(bandDao, albumDao, trackDao, aggregatorActor))
}
