package ru.otus.sc.route

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, Scheduler}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import ru.otus.sc.actor.GateActor
import ru.otus.sc.actor.GateActor.FindByNameContaining
import ru.otus.sc.model.dto.SearchDto
import ru.otus.sc.route.interfaces.BaseRouter
import ru.otus.sc.route.util.Implicits._
import sttp.tapir.json.circe._
import sttp.tapir.server.akkahttp._
import sttp.tapir.{Endpoint, _}

import scala.concurrent.ExecutionContext

class SearchRouter(gateActor: ActorRef[GateActor.Message])
                  (implicit executionContext: ExecutionContext, timeout: Timeout, scheduler: Scheduler) extends BaseRouter {
  override lazy val route: Route = searchEndpoint.toRoute { searchString =>
    toEither(gateActor.ask((ref: ActorRef[GateActor.Result]) => FindByNameContaining(searchString, ref))
      .map {
        case GateActor.Result(string, bands, albums, tracks) => SearchDto(string, bands, albums, tracks)
      })
  }

  override lazy val endpoints: List[Endpoint[_, _, _, _]] = List(searchEndpoint)

  private val searchEndpoint = endpoint
    .tag("search")
    .get
    .in("search")
    .in(path[String]("search"))
    .out(jsonBody[SearchDto])
}
