package ru.otus.sc.dao

import ru.otus.sc.model.entity.MusicEntities.{AlbumRow, BandRow}

import scala.concurrent.Future

/**
 * Band DAO
 */
trait BandDao extends AbstractDao[BandRow, (BandRow, AlbumRow)] {
  def getBySinger(name: String): Future[List[(BandRow, AlbumRow)]]
}
