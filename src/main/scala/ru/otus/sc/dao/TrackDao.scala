package ru.otus.sc.dao

import java.util.UUID

import ru.otus.sc.model.entity.MusicEntities.TrackRow

import scala.concurrent.Future

/**
 * Track DAO
 */
trait TrackDao extends AbstractDao[TrackRow, TrackRow] {
  def getByAlbum(albumID: UUID): Future[List[TrackRow]]

  def getByAlbumName(albumName: String): Future[List[TrackRow]]
}
