package ru.otus.sc.route

import java.util.UUID

import cats.syntax.semigroupk._
import org.http4s.HttpRoutes
import org.http4s.syntax.kleisli._
import ru.otus.sc.model.dto.{TrackAddDto, TrackGetDto, TrackUpdateDto}
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.route.util.Implicits._
import ru.otus.sc.route.util.{HttpException, InternalServerError, NotFound}
import ru.otus.sc.service.TrackService
import sttp.model.StatusCode
import sttp.tapir.Endpoint
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.http4s.ztapir._
import sttp.tapir.ztapir.{ZEndpoint, _}
import zio.Task
import zio.interop.catz._
import zio.interop.catz.implicits._

class TrackRouterImpl(trackService: TrackService.Service)(implicit serverOptions: Http4sServerOptions[Task])
  extends MusicRouter {

  override lazy val route: HttpRoutes[Task] = getTrackByIdRoute <+> getByAlbumIdRoute <+> getTrackByNameRoute <+>
    getAllTracksRoute <+> getTracksByAlbumNameRoute <+> addTrackRoute <+> updateTrackRoute <+> deleteTrackRoute

  override lazy val endpoints: List[ZEndpoint[_, _, _]] = List(getTrackById, getByAlbumId, getTrackByName,
    getAllTracks, getTracksByAlbumName, addTrack, updateTrack, deleteTrack)

  private val trackEndpoint: Endpoint[Unit, HttpException, Unit, Any] =
    endpoint.in("track").tag("track").errorOut(
      oneOf[HttpException](
        statusMapping(StatusCode.NotFound, jsonBody[NotFound].description("Not found")),
        statusMapping(StatusCode.InternalServerError, jsonBody[InternalServerError].description("Internal server Error"))
      )
    )

  private val getTrackById = trackEndpoint
    .get
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[TrackGetDto])

  private val getTrackByIdRoute = getTrackById.toRoutes(trackService.getById)

  private val getByAlbumId = trackEndpoint
    .get
    .in("albumId")
    .in(path[UUID]("albumId"))
    .out(jsonBody[List[TrackGetDto]])

  private val getByAlbumIdRoute = getByAlbumId.toRoutes(trackService.getByAlbum)

  private val getTrackByName = trackEndpoint
    .get
    .in("name")
    .in(path[String]("name"))
    .out(jsonBody[List[TrackGetDto]])

  private val getTrackByNameRoute = getTrackByName.toRoutes(trackService.getByName)

  private val getAllTracks = trackEndpoint
    .get
    .out(jsonBody[List[TrackGetDto]])

  private val getAllTracksRoute = getAllTracks.toRoutes(_ => trackService.getAll)

  private val getTracksByAlbumName = trackEndpoint
    .get
    .in("albumName")
    .in(path[String]("albumName"))
    .out(jsonBody[List[TrackGetDto]])

  private val getTracksByAlbumNameRoute = getTracksByAlbumName.toRoutes(trackService.getByAlbumName)

  private val addTrack = trackEndpoint
    .post
    .in(jsonBody[TrackAddDto])
    .out(jsonBody[Boolean])

  private val addTrackRoute = addTrack.toRoutes(trackService.add)

  private val updateTrack = trackEndpoint
    .put
    .in(jsonBody[TrackUpdateDto])
    .out(jsonBody[Boolean])

  private val updateTrackRoute = updateTrack.toRoutes(trackService.update)

  private val deleteTrack = trackEndpoint
    .delete
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[Boolean])

  private val deleteTrackRoute = deleteTrack.toRoutes(trackService.delete)
}
