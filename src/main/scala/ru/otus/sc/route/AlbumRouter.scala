package ru.otus.sc.route

import java.util.UUID

import akka.http.scaladsl.server.Directives._enhanceRouteWithConcatenation
import akka.http.scaladsl.server.Route
import ru.otus.sc.model.dto.{AlbumAddDto, AlbumGetDto, AlbumUpdateDto}
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.route.util.Implicits._
import ru.otus.sc.service.AlbumService
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.akkahttp._

import scala.concurrent.ExecutionContext


class AlbumRouter(albumService: AlbumService)(implicit executionContext: ExecutionContext) extends MusicRouter {

  override lazy val route: Route = getByIdRoute ~ getByBandIdRoute ~ getAllAlbumsRoute ~ getAlbumByNameRoute ~
    addAlbumRoute ~ updateAlbumRoute ~ deleteAlbumRoute

  override lazy val endpoints: List[Endpoint[_, _, _, _]] = List(getById, getByBandId, getAllAlbums, getAlbumByName,
    addAlbum, updateAlbum, deleteAlbum)

  private val albumEndpoint = endpoint.in("album").tag("album")

  private val getById = albumEndpoint
    .get
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[AlbumGetDto])
    .errorOut(statusCode(StatusCode.NotFound))

  private val getByIdRoute = getById.toRoute(albumService.getById)

  private val getByBandId = albumEndpoint
    .get
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[List[AlbumGetDto]])

  private val getByBandIdRoute = getByBandId.toRoute(albumService.getByBand)

  private val getAllAlbums = albumEndpoint
    .get
    .out(jsonBody[List[AlbumGetDto]])

  private val getAllAlbumsRoute = getAllAlbums.toRoute(_ => albumService.getAll)

  private val getAlbumByName = albumEndpoint
    .get
    .in("name")
    .in(path[String]("name"))
    .out(jsonBody[List[AlbumGetDto]])

  private val getAlbumByNameRoute = getAlbumByName.toRoute(albumService.getByName)

  private val addAlbum = albumEndpoint
    .post
    .in(jsonBody[AlbumAddDto])
    .out(jsonBody[Boolean])

  private val addAlbumRoute = addAlbum.toRoute(albumService.add)

  private val updateAlbum = albumEndpoint
    .put
    .in(jsonBody[AlbumUpdateDto])
    .out(jsonBody[Boolean])

  private val updateAlbumRoute = updateAlbum.toRoute(albumService.update)

  private val deleteAlbum = albumEndpoint
    .delete
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[Boolean])

  private val deleteAlbumRoute = deleteAlbum.toRoute(albumService.delete)
}
