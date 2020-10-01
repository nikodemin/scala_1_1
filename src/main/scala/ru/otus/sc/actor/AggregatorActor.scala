package ru.otus.sc.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import ru.otus.sc.model.dto.{AlbumGetDto, BandGetDto, TrackGetDto}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

object AggregatorActor {

  sealed trait Message

  case class Timeout(string: String) extends Message

  case class Result(string: String, bands: List[BandGetDto] = List.empty, albums: List[AlbumGetDto] = List.empty,
                    tracks: List[TrackGetDto] = List.empty) extends Message

  def apply(resultMap: Map[Result, Int] = Map.empty, duration: FiniteDuration, gateActor: ActorRef[GateActor.Message],
            cachingActor: ActorRef[CachingActor.Message])(implicit executionContext: ExecutionContext): Behavior[Message] = {
    Behaviors.setup {
      ctx => {
        Behaviors.receiveMessage {
          case res: Result =>
            val newMap = resultMap.find(_._1.string == res.string)
              .fold {
                ctx.system.scheduler.scheduleOnce(duration, () => ctx.self ! Timeout(res.string))
                resultMap + (res -> 1)
              } {
                oldRes: (Result, Int) =>
                  resultMap.filterNot(_._1.string == res.string) + (oldRes._1.copy(
                    albums = oldRes._1.albums.prependedAll(res.albums),
                    bands = oldRes._1.bands.prependedAll(res.bands),
                    tracks = oldRes._1.tracks.prependedAll(res.tracks)
                  ) -> (oldRes._2 + 1))
              }

            if (newMap.find(_._1.string == res.string) == 3) {
              ctx.self ! Timeout(res.string)
            }

            apply(newMap, duration, gateActor, cachingActor)

          case Timeout(key) =>
            resultMap.find(_._1.string == key).map(_._1).fold(Behaviors.same[Message]) { result =>
              cachingActor ! CachingActor.Cache(result)
              gateActor ! GateActor.Result(result.string, result.bands, result.albums, result.tracks)
              apply(resultMap - result, duration, gateActor, cachingActor)
            }
        }
      }
    }
  }
}
