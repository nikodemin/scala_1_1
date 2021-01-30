package ru.otus.sc.route.interfaces

import ru.otus.sc.route.{AlbumRouterImpl, BandRouterImpl, TrackRouterImpl}
import ru.otus.sc.service.AlbumService.AlbumService
import ru.otus.sc.service.BandService.BandService
import ru.otus.sc.service.TrackService.TrackService
import ru.otus.sc.service.{AlbumService, BandService, TrackService}
import sttp.tapir.server.http4s.Http4sServerOptions
import zio.{Has, RLayer, Task, ZLayer}

trait MusicRouter extends BaseRouter

object MusicRouter {
  type ServerConfig = Has[Http4sServerOptions[Task]]
  type AlbumRouter = Has[AlbumRouterImpl]
  type BandRouter = Has[BandRouterImpl]
  type TrackRouter = Has[TrackRouterImpl]

  val bandRouterLive: RLayer[BandService with ServerConfig, BandRouter] = ZLayer.fromFunction(env => {
    implicit val config: Http4sServerOptions[Task] = env.get[Http4sServerOptions[Task]]
    new BandRouterImpl(env.get[BandService.Service])
  })

  val albumRouterLive: RLayer[AlbumService with ServerConfig, AlbumRouter] = ZLayer.fromFunction(env => {
    implicit val config: Http4sServerOptions[Task] = env.get[Http4sServerOptions[Task]]
    new AlbumRouterImpl(env.get[AlbumService.Service])
  })

  val trackRouterLive: RLayer[TrackService with ServerConfig, TrackRouter] = ZLayer.fromFunction(env => {
    implicit val config: Http4sServerOptions[Task] = env.get[Http4sServerOptions[Task]]
    new TrackRouterImpl(env.get[TrackService.Service])
  })
}
