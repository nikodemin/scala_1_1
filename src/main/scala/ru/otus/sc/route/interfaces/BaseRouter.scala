package ru.otus.sc.route.interfaces

import org.http4s.HttpRoutes
import sttp.tapir.ztapir.ZEndpoint
import zio.Task

trait BaseRouter {
  def route: HttpRoutes[Task]

  def endpoints: List[ZEndpoint[_, _, _]]
}
