package ru.otus.sc.service

import java.util.UUID

import ru.otus.sc.model.dto.{TrackAddDto, TrackGetDto, TrackUpdateDto}

import scala.concurrent.Future

trait TrackService {
  def getAll: Future[List[TrackGetDto]]

  def getByAlbum(albumID: UUID): Future[List[TrackGetDto]]

  def getByName(name: String): Future[List[TrackGetDto]]

  def getByAlbumName(name: String): Future[List[TrackGetDto]]

  def getById(id: UUID): Future[Option[TrackGetDto]]

  def add(trackAddDto: TrackAddDto): Future[Boolean]

  def update(trackUpdateDto: TrackUpdateDto): Future[Boolean]

  def delete(id: UUID): Future[Boolean]
}
