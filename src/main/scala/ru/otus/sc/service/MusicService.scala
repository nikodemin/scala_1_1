package ru.otus.sc.service

import java.util.UUID

import ru.otus.sc.model.entity.{Album, Band, Track}

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

  def getBandByName(name: String): Option[Band]

  def getAlbumByName(name: String): Option[Album]

  def getTracksByAlbumName(name: String): List[Track]

  def addBand(band: Band): Boolean

  def addAlbum(bandId: UUID, album: Album): Boolean

  def addTrack(albumId: UUID, track: Track): Boolean

  def updateBand(band: Band): Boolean

  def updateAlbum(album: Album): Boolean

  def updateTrack(track: Track): Boolean

  def deleteBand(id: UUID): Boolean

  def deleteAlbum(id: UUID): Boolean

  def deleteTrack(id: UUID): Boolean
}
