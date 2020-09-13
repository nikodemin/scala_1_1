package ru.otus.sc.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ru.otus.sc.route.interfaces.{BaseRouter, MusicRouter}

class MusicRouterGuard(musicRouters: List[MusicRouter]) extends BaseRouter {
  override def route: Route = pathPrefix("music") {
    musicRouters.map(_.route).reduceLeft(_ ~ _)
  }
}
