package ru.otus.sc.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.{Json, OFormat}
import ru.otus.sc.model.dto._
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.service.BandService

class BandRouter(bandService: BandService) extends MusicRouter {
  private implicit lazy val trackGetDtoFormat: OFormat[TrackGetDto] = Json.format
  private implicit lazy val albumGetDtoFormat: OFormat[AlbumGetDto] = Json.format
  private implicit lazy val bandGetDtoFormat: OFormat[BandGetDto] = Json.format
  private implicit lazy val bandAddDtoFormat: OFormat[BandAddDto] = Json.format
  private implicit lazy val bandUpdateDtoFormat: OFormat[BandUpdateDto] = Json.format

  override def route: Route = pathPrefix("band") {
    getBandByID ~ getAllBands ~ getBandByName ~ getBandBySinger ~ addBand ~ updateBand ~ deleteBand
  }

  private val getBandByID: Route = (get & path("id" / JavaUUID) & pathEnd) { id =>
    onSuccess(bandService.getById(id)) {
      case Some(band) => complete(band)
      case None => complete(StatusCodes.NotFound)
    }
  }

  private val getBandByName: Route = (get & path("name" / Segment) & pathEnd) { name =>
    onSuccess(bandService.getByName(name)) {
      case Some(band) => complete(band)
      case None => complete(StatusCodes.NotFound)
    }
  }

  private val getBandBySinger: Route = (get & path("singerName" / Segment) & pathEnd) { name =>
    onSuccess(bandService.getBySinger(name)) {
      list: List[BandGetDto] => complete(list)
    }
  }

  private val getAllBands: Route = (get & pathEnd) {
    onSuccess(bandService.getAll) {
      list: List[BandGetDto] => complete(list)
    }
  }

  private val addBand: Route = (post & entity(as[BandAddDto]) & pathEnd) { band =>
    onSuccess(bandService.add(band)) {
      case true => complete(StatusCodes.Created)
      case false => complete(StatusCodes.BadRequest)
    }
  }

  private val updateBand: Route = (put & entity(as[BandUpdateDto]) & pathEnd) { band =>
    onSuccess(bandService.update(band)) {
      case true => complete(StatusCodes.OK)
      case false => complete(StatusCodes.BadRequest)
    }
  }

  private val deleteBand: Route = (delete & path("id" / JavaUUID) & pathEnd) { id =>
    onSuccess(bandService.delete(id)) {
      case true => complete(StatusCodes.OK)
      case false => complete(StatusCodes.BadRequest)
    }
  }
}
