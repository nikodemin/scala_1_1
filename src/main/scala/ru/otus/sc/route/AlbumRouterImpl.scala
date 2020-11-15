package ru.otus.sc.route

import java.util.UUID

import cats.syntax.semigroupk._
import org.http4s.HttpRoutes
import org.http4s.syntax.kleisli._
import ru.otus.sc.model.dto.{AlbumAddDto, AlbumGetDto, AlbumUpdateDto}
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.route.util.Implicits._
import ru.otus.sc.route.util.{HttpException, InternalServerError, NotFound}
import ru.otus.sc.service.AlbumService
import sttp.model.StatusCode
import sttp.tapir.Endpoint
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.http4s.ztapir._
import sttp.tapir.ztapir._
import zio.Task
import zio.interop.catz._
import zio.interop.catz.implicits._


class AlbumRouterImpl(albumService: AlbumService.Service)(implicit serverOptions: Http4sServerOptions[Task])
  extends MusicRouter {

  override lazy val route: HttpRoutes[Task] = getByIdRoute <+> getByBandIdRoute <+> getAllAlbumsRoute <+>
    getAlbumByNameRoute <+> addAlbumRoute <+> updateAlbumRoute <+> deleteAlbumRoute

  override lazy val endpoints: List[ZEndpoint[_, _, _]] = List(getById, getByBandId, getAllAlbums, getAlbumByName,
    addAlbum, updateAlbum, deleteAlbum)

  private val albumEndpoint: Endpoint[Unit, HttpException, Unit, Any] =
    endpoint.in("album").tag("album").errorOut(
      oneOf[HttpException](
        statusMapping(StatusCode.NotFound, jsonBody[NotFound]),
        statusMapping(StatusCode.InternalServerError, jsonBody[InternalServerError])
      )
    )

  private val getById = albumEndpoint
    .get
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[AlbumGetDto])

  private val getByIdRoute = getById.toRoutes(albumService.getById)

  private val getByBandId = albumEndpoint
    .get
    .in("bandId")
    .in(path[UUID]("bandId"))
    .out(jsonBody[List[AlbumGetDto]])

  private val getByBandIdRoute = getByBandId.toRoutes(albumService.getByBand)

  private val getAllAlbums = albumEndpoint
    .get
    .out(jsonBody[List[AlbumGetDto]])

  private val getAllAlbumsRoute = getAllAlbums.toRoutes(_ => albumService.getAll)

  private val getAlbumByName = albumEndpoint
    .get
    .in("name")
    .in(path[String]("name"))
    .out(jsonBody[List[AlbumGetDto]])

  private val getAlbumByNameRoute = getAlbumByName.toRoutes(albumService.getByName)

  private val addAlbum = albumEndpoint
    .post
    .in(jsonBody[AlbumAddDto])
    .out(jsonBody[Boolean])

  private val addAlbumRoute = addAlbum.toRoutes(albumService.add)

  private val updateAlbum = albumEndpoint
    .put
    .in(jsonBody[AlbumUpdateDto])
    .out(jsonBody[Boolean])

  private val updateAlbumRoute = updateAlbum.toRoutes(albumService.update)

  private val deleteAlbum = albumEndpoint
    .delete
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[Boolean])

  private val deleteAlbumRoute = deleteAlbum.toRoutes(albumService.delete)
}
