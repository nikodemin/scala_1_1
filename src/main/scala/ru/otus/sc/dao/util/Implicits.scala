package ru.otus.sc.dao.util

import slick.basic.BasicBackend
import slick.jdbc.PostgresProfile.api._
import zio.{Task, ZIO}

object Implicits {
  implicit def seqDBIOToTask[T](action: DBIO[Seq[T]])(implicit db: BasicBackend#DatabaseDef): Task[List[T]] =
    ZIO.fromFuture(ec => db.run(action)).map(_.toList)

  implicit def intDBIOToTask(action: DBIO[Int])(implicit db: BasicBackend#DatabaseDef): Task[Boolean] =
    ZIO.fromFuture(ec => db.run(action)).map(_ > 0)

  implicit def elDBIOToTask[T](action: DBIO[T])(implicit db: BasicBackend#DatabaseDef): Task[T] =
    ZIO.fromFuture(ec => db.run(action))
}
