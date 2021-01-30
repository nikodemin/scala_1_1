package ru.otus.sc.service

import java.util.UUID

import ru.otus.sc.dao.BandDao.BandDao
import ru.otus.sc.model.dto._
import ru.otus.sc.service.impl.BandServiceImpl
import zio.{Has, RLayer, Task, ZLayer}

object BandService {
  type BandService = Has[Service]

  trait Service {
    def getAll: Task[List[BandGetDto]]

    def getByName(name: String): Task[Option[BandGetDto]]

    def getBySinger(name: String): Task[List[BandGetDto]]

    def getById(id: UUID): Task[Option[BandGetDto]]

    def add(band: BandAddDto): Task[Boolean]

    def update(band: BandUpdateDto): Task[Boolean]

    def delete(id: UUID): Task[Boolean]
  }

  val live: RLayer[BandDao, BandService] = ZLayer.fromFunction(dao => new BandServiceImpl(dao.get))
}
