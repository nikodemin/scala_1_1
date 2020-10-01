package ru.otus.sc.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import ru.otus.sc.actor.AggregatorActor.Result

object CachingActor {

  sealed trait Message

  case class Cache(result: Result) extends Message

  case class Get(string: String, replyTo: ActorRef[GateActor.Message]) extends Message

  def apply(cache: Map[String, Result] = Map.empty): Behavior[Message] = Behaviors.setup {
    ctx => {
      Behaviors.receiveMessage {
        case Cache(result) => apply(cache + (result.string -> result))

        case Get(string, replyTo) =>
          cache.get(string).fold(replyTo ! GateActor.NotFoundInCache(string)) {
            res => replyTo ! GateActor.Result(res.string, res.bands, res.albums, res.tracks)
          }
          Behaviors.same
      }
    }
  }
}
