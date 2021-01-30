package ru.otus.sc.service

import java.util.UUID

import ru.otus.sc.dao.AlbumDao.AlbumDao
import ru.otus.sc.model.dto.{AlbumAddDto, AlbumGetDto, AlbumUpdateDto}
import ru.otus.sc.service.impl.AlbumServiceImpl
import zio.{Has, RLayer, Task, ZLayer}

object AlbumService {
  type AlbumService = Has[Service]

  trait Service {
    def getByBand(bandId: UUID): Task[List[AlbumGetDto]]

    def getAll: Task[List[AlbumGetDto]]

    def getByName(name: String): Task[List[AlbumGetDto]]

    def getById(id: UUID): Task[Option[AlbumGetDto]]

    def add(albumAddDto: AlbumAddDto): Task[Boolean]

    def update(albumUpdateDto: AlbumUpdateDto): Task[Boolean]

    def delete(id: UUID): Task[Boolean]
  }

  val live: RLayer[AlbumDao, AlbumService] = ZLayer.fromFunction(dao => new AlbumServiceImpl(dao.get))
}
