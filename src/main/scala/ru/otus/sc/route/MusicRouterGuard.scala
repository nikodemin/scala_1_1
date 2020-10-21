package ru.otus.sc.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ru.otus.sc.route.interfaces.{BaseRouter, MusicRouter}
import sttp.tapir.{Endpoint, _}

class MusicRouterGuard(musicRouters: List[MusicRouter], musicEndpoints: List[Endpoint[_, _, _, _]]) extends BaseRouter {
  override def route: Route = pathPrefix("music") {
    musicRouters.map(_.route).reduceLeft(_ ~ _)
  }

  override def endpoints: List[Endpoint[_, _, _, _]] = musicEndpoints.map(_.prependIn("music"))
}
