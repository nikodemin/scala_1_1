package ru.otus.sc.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.{Json, OFormat}
import ru.otus.sc.model.dto.{AlbumAddDto, AlbumGetDto, AlbumUpdateDto, TrackGetDto}
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.service.AlbumService

class AlbumRouter(albumService: AlbumService) extends MusicRouter {
  private implicit lazy val trackGetDtoFormat: OFormat[TrackGetDto] = Json.format
  private implicit lazy val albumGetFormat: OFormat[AlbumGetDto] = Json.format
  private implicit lazy val albumAddFormat: OFormat[AlbumAddDto] = Json.format
  private implicit lazy val albumUpdateFormat: OFormat[AlbumUpdateDto] = Json.format

  override def route: Route = pathPrefix("album") {
    getById ~ getByBandId ~ getAllAlbums ~ getAlbumByName ~ addAlbum ~ updateAlbum ~ deleteAlbum
  }

  private val getById: Route = (get & path("id" / JavaUUID) & pathEnd) { id =>
    onSuccess(albumService.getById(id)) {
      case None => complete(StatusCodes.NotFound)
      case Some(album) => complete(album)
    }
  }

  private val getByBandId: Route = (get & path("bandID" / JavaUUID)) { bandId =>
    onSuccess(albumService.getByBand(bandId)) {
      list: List[AlbumGetDto] => complete(list)
    }
  }

  private val getAllAlbums: Route = (get & pathEnd) {
    onSuccess(albumService.getAll) {
      list: List[AlbumGetDto] => complete(list)
    }
  }

  private val getAlbumByName: Route = (get & path("name" / Segment) & pathEnd) { name =>
    onSuccess(albumService.getByName(name)) {
      list: List[AlbumGetDto] => complete(list)
    }
  }

  private val addAlbum: Route = (post & entity(as[AlbumAddDto]) & pathEnd) { album =>
    onSuccess(albumService.add(album)) {
      case true => complete(StatusCodes.Created)
      case false => complete(StatusCodes.BadRequest)
    }
  }

  private val updateAlbum: Route = (put & entity(as[AlbumUpdateDto]) & pathEnd) { album =>
    onSuccess(albumService.update(album)) {
      case true => complete(StatusCodes.OK)
      case false => complete(StatusCodes.BadRequest)
    }
  }

  private val deleteAlbum: Route = (delete & path("id" / JavaUUID) & pathEnd) { albumId =>
    onSuccess(albumService.delete(albumId)) {
      case true => complete(StatusCodes.OK)
      case false => complete(StatusCodes.BadRequest)
    }
  }
}
