package ru.otus.sc.service

import ru.otus.sc.model.entity.{Album, Track}

/**
 * Music service
 */
trait MusicService {
  /**
   * Get all albums
   *
   * @return List[Album]
   */
  def getAllAlbums: List[Album]

  /**
   * Get tracks by band name
   *
   * @param name String
   * @return List[Track]
   */
  def getTracksByBandName(name: String): List[Track]

  /**
   * Get tracks by singer name
   *
   * @param name String
   * @return List[Track]
   */
  def getTracksBySingerName(name: String): List[Track]
}
