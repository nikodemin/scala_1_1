package ru.otus.sc.route

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, Scheduler}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.{Json, OFormat}
import ru.otus.sc.actor.GateActor
import ru.otus.sc.actor.GateActor.FindByNameContaining
import ru.otus.sc.model.dto.{AlbumGetDto, BandGetDto, SearchDto, TrackGetDto}
import ru.otus.sc.route.interfaces.BaseRouter

import scala.concurrent.{ExecutionContext, Future}

class SearchRouter(gateActor: ActorRef[GateActor.Message])
                  (implicit executionContext: ExecutionContext, timeout: Timeout, scheduler: Scheduler) extends BaseRouter {
  private implicit lazy val trackGetDtoFormat: OFormat[TrackGetDto] = Json.format
  private implicit lazy val albumGetDtoFormat: OFormat[AlbumGetDto] = Json.format
  private implicit lazy val bandGetDtoFormat: OFormat[BandGetDto] = Json.format
  private implicit lazy val searchDtoFormat: OFormat[SearchDto] = Json.format

  override def route: Route = (get & path("search" / Segment)) { searchString =>
    val res: Future[GateActor.Result] = gateActor.ask((ref: ActorRef[GateActor.Result]) => FindByNameContaining(searchString, ref))
    onSuccess(res) {
      case GateActor.Result(string, bands, albums, tracks) => complete(SearchDto(string, bands, albums, tracks))
    }
  }
}
