package ru.otus.sc.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.{Json, OFormat}
import ru.otus.sc.model.dto.{TrackAddDto, TrackGetDto, TrackUpdateDto}
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.service.TrackService

class TrackRouter(trackService: TrackService) extends MusicRouter {
  private implicit lazy val trackGetDtoFormat: OFormat[TrackGetDto] = Json.format
  private implicit lazy val trackUpdateDtoFormat: OFormat[TrackUpdateDto] = Json.format
  private implicit lazy val trackAddDtoFormat: OFormat[TrackAddDto] = Json.format

  override def route: Route = pathPrefix("track") {
    getByAlbumId ~ getTrackByName ~ getTrackById ~ getTracksByAlbumName ~ getAllTracks ~ addTrack ~ updateTrack ~ deleteTrack
  }

  private val getTrackById: Route = (get & path("id" / JavaUUID) & pathEnd) { id =>
    onSuccess(trackService.getById(id)) {
      case None => complete(StatusCodes.NotFound)
      case Some(track) => complete(track)
    }
  }

  private val getByAlbumId: Route = (get & path("albumID" / JavaUUID)) { albumID =>
    onSuccess(trackService.getByAlbum(albumID)) {
      list: List[TrackGetDto] => complete(list)
    }
  }

  private val getTrackByName: Route = (get & path("name" / Segment) & pathEnd) { name =>
    onSuccess(trackService.getByName(name)) {
      list: List[TrackGetDto] => complete(list)
    }
  }

  private val getAllTracks: Route = (get & pathEnd) {
    onSuccess(trackService.getAll) {
      list: List[TrackGetDto] => complete(list)
    }
  }

  private val getTracksByAlbumName: Route = (get & path("albumName" / Segment) & pathEnd) { albumName =>
    onSuccess(trackService.getByAlbumName(albumName)) {
      list: List[TrackGetDto] => complete(list)
    }
  }

  private val addTrack: Route = (post & entity(as[TrackAddDto]) & pathEnd) { track =>
    onSuccess(trackService.add(track)) {
      case true => complete(StatusCodes.Created)
      case false => complete(StatusCodes.BadRequest)
    }
  }

  private val updateTrack: Route = (put & entity(as[TrackUpdateDto]) & pathEnd) { track =>
    onSuccess(trackService.update(track)) {
      case true => complete(StatusCodes.OK)
      case false => complete(StatusCodes.BadRequest)
    }
  }

  private val deleteTrack: Route = (delete & path("id" / JavaUUID) & pathEnd) { trackId =>
    onSuccess(trackService.delete(trackId)) {
      case true => complete(StatusCodes.OK)
      case false => complete(StatusCodes.BadRequest)
    }
  }
}
