package ru.otus.sc.dao

import java.util.UUID

import ru.otus.sc.model.entity.MusicEntities.{AlbumRow, TrackRow}

import scala.concurrent.Future

/**
 * Album DAO
 */
trait AlbumDao extends AbstractDao[AlbumRow, (AlbumRow, TrackRow)] {
  def getByBand(bandId: UUID): Future[List[(AlbumRow, TrackRow)]]
}
