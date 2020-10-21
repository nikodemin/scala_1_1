package ru.otus.sc.route

import java.util.UUID

import akka.http.scaladsl.server.Directives._enhanceRouteWithConcatenation
import akka.http.scaladsl.server.Route
import ru.otus.sc.model.dto._
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.route.util.Implicits._
import ru.otus.sc.service.BandService
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.akkahttp._

import scala.concurrent.ExecutionContext

class BandRouter(bandService: BandService)(implicit executionContext: ExecutionContext) extends MusicRouter {
  override lazy val route: Route = getBandByIdRoute ~ getBandByNameRoute ~ getBandBySingerRoute ~
    getAllBandsRoute ~ addBandRoute ~ updateBandRoute ~ deleteBandRoute

  override lazy val endpoints: List[Endpoint[_, _, _, _]] = List(getBandById, getBandByName, getBandBySinger,
    getAllBands, addBand, updateBand, deleteBand)

  private val bandEndpoint = endpoint.in("band").tag("band")

  private val getBandById = bandEndpoint
    .get
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[BandGetDto])
    .errorOut(statusCode(StatusCode.NotFound))

  private val getBandByIdRoute = getBandById.toRoute(bandService.getById)

  private val getBandByName = bandEndpoint
    .get
    .in("name")
    .in(path[String]("name"))
    .out(jsonBody[BandGetDto])
    .errorOut(statusCode(StatusCode.NotFound))

  private val getBandByNameRoute = getBandByName.toRoute(bandService.getByName)

  private val getBandBySinger = bandEndpoint
    .get
    .in("singer")
    .in(path[String]("singer"))
    .out(jsonBody[List[BandGetDto]])

  private val getBandBySingerRoute = getBandBySinger.toRoute(bandService.getBySinger)

  private val getAllBands = bandEndpoint
    .get
    .out(jsonBody[List[BandGetDto]])

  private val getAllBandsRoute = getAllBands.toRoute(_ => bandService.getAll)

  private val addBand = bandEndpoint
    .post
    .in(jsonBody[BandAddDto])
    .out(jsonBody[Boolean])

  private val addBandRoute = addBand.toRoute(bandService.add)

  private val updateBand = bandEndpoint
    .put
    .in(jsonBody[BandUpdateDto])
    .out(jsonBody[Boolean])

  private val updateBandRoute = updateBand.toRoute(bandService.update)

  private val deleteBand = bandEndpoint
    .delete
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[Boolean])

  private val deleteBandRoute = deleteBand.toRoute(bandService.delete)
}
