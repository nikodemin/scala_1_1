package ru.otus.sc.route.interfaces

import akka.http.scaladsl.server.Route

trait BaseRouter {
  def route: Route
}
