package ru.otus.sc.service

import java.util.UUID

import ru.otus.sc.dao.TrackDao.TrackDao
import ru.otus.sc.model.dto.{TrackAddDto, TrackGetDto, TrackUpdateDto}
import ru.otus.sc.service.impl.TrackServiceImpl
import zio.{Has, RLayer, Task, ZLayer}

object TrackService {
  type TrackService = Has[Service]

  trait Service {
    def getAll: Task[List[TrackGetDto]]

    def getByAlbum(albumID: UUID): Task[List[TrackGetDto]]

    def getByName(name: String): Task[List[TrackGetDto]]

    def getByAlbumName(name: String): Task[List[TrackGetDto]]

    def getById(id: UUID): Task[Option[TrackGetDto]]

    def add(trackAddDto: TrackAddDto): Task[Boolean]

    def update(trackUpdateDto: TrackUpdateDto): Task[Boolean]

    def delete(id: UUID): Task[Boolean]
  }

  val live: RLayer[TrackDao, TrackService] = ZLayer.fromFunction(dao => new TrackServiceImpl(dao.get))
}