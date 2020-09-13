package ru.otus.sc.service

import java.util.UUID

import ru.otus.sc.model.dto._

import scala.concurrent.Future

trait BandService {
  def getAll: Future[List[BandGetDto]]

  def getByName(name: String): Future[Option[BandGetDto]]

  def getBySinger(name: String): Future[List[BandGetDto]]

  def getById(id: UUID): Future[Option[BandGetDto]]

  def add(band: BandAddDto): Future[Boolean]

  def update(band: BandUpdateDto): Future[Boolean]

  def delete(id: UUID): Future[Boolean]
}
