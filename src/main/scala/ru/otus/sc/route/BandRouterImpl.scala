package ru.otus.sc.route

import java.util.UUID

import cats.syntax.semigroupk._
import org.http4s.HttpRoutes
import org.http4s.syntax.kleisli._
import ru.otus.sc.model.dto._
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.route.util.Implicits._
import ru.otus.sc.route.util.{HttpException, InternalServerError, NotFound}
import ru.otus.sc.service.BandService
import sttp.model.StatusCode
import sttp.tapir.Endpoint
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.http4s.ztapir._
import sttp.tapir.ztapir.{ZEndpoint, endpoint, oneOf, statusMapping, _}
import zio.Task
import zio.interop.catz._
import zio.interop.catz.implicits._

class BandRouterImpl(bandService: BandService.Service)(implicit serverOptions: Http4sServerOptions[Task]) extends MusicRouter {
  override lazy val route: HttpRoutes[Task] = getBandByIdRoute <+> getBandByNameRoute <+> getBandBySingerRoute <+>
    getAllBandsRoute <+> addBandRoute <+> updateBandRoute <+> deleteBandRoute

  override lazy val endpoints: List[ZEndpoint[_, _, _]] = List(getBandById, getBandByName, getBandBySinger,
    getAllBands, addBand, updateBand, deleteBand)

  private val bandEndpoint: Endpoint[Unit, HttpException, Unit, Any] =
    endpoint.in("band").tag("band").errorOut(
      oneOf[HttpException](
        statusMapping(StatusCode.NotFound, jsonBody[NotFound].description("Not found")),
        statusMapping(StatusCode.InternalServerError, jsonBody[InternalServerError].description("Internal server Error"))
      )
    )

  private val getBandById = bandEndpoint
    .get
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[BandGetDto])

  private val getBandByIdRoute = getBandById.toRoutes(bandService.getById)

  private val getBandByName = bandEndpoint
    .get
    .in("name")
    .in(path[String]("name"))
    .out(jsonBody[BandGetDto])

  private val getBandByNameRoute = getBandByName.toRoutes(bandService.getByName)

  private val getBandBySinger = bandEndpoint
    .get
    .in("singer")
    .in(path[String]("singer"))
    .out(jsonBody[List[BandGetDto]])

  private val getBandBySingerRoute = getBandBySinger.toRoutes(bandService.getBySinger)

  private val getAllBands = bandEndpoint
    .get
    .out(jsonBody[List[BandGetDto]])

  private val getAllBandsRoute = getAllBands.toRoutes(_ => bandService.getAll)

  private val addBand = bandEndpoint
    .post
    .in(jsonBody[BandAddDto])
    .out(jsonBody[Boolean])

  private val addBandRoute = addBand.toRoutes(bandService.add)

  private val updateBand = bandEndpoint
    .put
    .in(jsonBody[BandUpdateDto])
    .out(jsonBody[Boolean])

  private val updateBandRoute = updateBand.toRoutes(bandService.update)

  private val deleteBand = bandEndpoint
    .delete
    .in("id")
    .in(path[UUID]("id"))
    .out(jsonBody[Boolean])

  private val deleteBandRoute = deleteBand.toRoutes(bandService.delete)
}
