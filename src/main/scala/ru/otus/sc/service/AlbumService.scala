package ru.otus.sc.service

import java.util.UUID

import ru.otus.sc.model.dto.{AlbumAddDto, AlbumGetDto, AlbumUpdateDto}

import scala.concurrent.Future

trait AlbumService {
  def getByBand(bandId: UUID): Future[List[AlbumGetDto]]

  def getAll: Future[List[AlbumGetDto]]

  def getByName(name: String): Future[List[AlbumGetDto]]

  def getById(id: UUID): Future[Option[AlbumGetDto]]

  def add(albumAddDto: AlbumAddDto): Future[Boolean]

  def update(albumUpdateDto: AlbumUpdateDto): Future[Boolean]

  def delete(id: UUID): Future[Boolean]
}
