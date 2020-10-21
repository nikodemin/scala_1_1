package ru.otus.sc.route

import java.util.UUID

import akka.http.scaladsl.server.Directives._enhanceRouteWithConcatenation
import akka.http.scaladsl.server.Route
import ru.otus.sc.model.dto.{TrackAddDto, TrackGetDto, TrackUpdateDto}
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.route.util.Implicits._
import ru.otus.sc.service.TrackService
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.akkahttp._

import scala.concurrent.ExecutionContext

class TrackRouter(trackService: TrackService)(implicit executionContext: ExecutionContext) extends MusicRouter {

  override lazy val route: Route = getTrackByIdRoute ~ getByAlbumIdRoute ~ getTrackByNameRoute ~ getAllTracksRoute ~
    getTracksByAlbumNameRoute ~ addTrackRoute ~ updateTrackRoute ~ deleteTrackRoute

  override lazy val endpoints: List[Endpoint[_, _, _, _]] = List(getTrackById, getByAlbumId, getTrackByName,
    getAllTracks, getTracksByAlbumName, addTrack, updateTrack, deleteTrack)

  private val trackEndpoint = endpoint.tag("track").in("track")

  private val getTrackById = trackEndpoint
    .get
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[TrackGetDto])
    .errorOut(statusCode(StatusCode.NotFound))

  private val getTrackByIdRoute = getTrackById.toRoute(trackService.getById)

  private val getByAlbumId = trackEndpoint
    .get
    .in("albumId")
    .in(path[UUID]("albumId"))
    .out(jsonBody[List[TrackGetDto]])

  private val getByAlbumIdRoute = getByAlbumId.toRoute(trackService.getByAlbum)

  private val getTrackByName = trackEndpoint
    .get
    .in("name")
    .in(path[String]("name"))
    .out(jsonBody[List[TrackGetDto]])

  private val getTrackByNameRoute = getTrackByName.toRoute(trackService.getByName)

  private val getAllTracks = trackEndpoint
    .get
    .out(jsonBody[List[TrackGetDto]])

  private val getAllTracksRoute = getAllTracks.toRoute(_ => trackService.getAll)

  private val getTracksByAlbumName = trackEndpoint
    .get
    .in("albumName")
    .in(path[String]("albumName"))
    .out(jsonBody[List[TrackGetDto]])

  private val getTracksByAlbumNameRoute = getTracksByAlbumName.toRoute(trackService.getByAlbumName)

  private val addTrack = trackEndpoint
    .post
    .in(jsonBody[TrackAddDto])
    .out(jsonBody[Boolean])

  private val addTrackRoute = addTrack.toRoute(trackService.add)

  private val updateTrack = trackEndpoint
    .put
    .in(jsonBody[TrackUpdateDto])
    .out(jsonBody[Boolean])

  private val updateTrackRoute = updateTrack.toRoute(trackService.update)

  private val deleteTrack = trackEndpoint
    .delete
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[Boolean])

  private val deleteTrackRoute = deleteTrack.toRoute(trackService.delete)
}
