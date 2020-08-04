package ru.otus.sc.dao

import ru.otus.sc.model.entity.{Album, Track}

/**
 * Track DAO
 */
trait TrackDao extends AbstractDao[Track] {
  def getByAlbum(album: Album): List[Track]
}
