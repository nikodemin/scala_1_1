package ru.otus.sc

import java.util.UUID

import ru.otus.sc.dao.impl._
import ru.otus.sc.model.dto.{GreetRequest, GreetResponse}
import ru.otus.sc.model.entity.{Album, Band, StorageValue, Track}
import ru.otus.sc.model.enums.Key.Key
import ru.otus.sc.service.impl.{EchoServiceImpl, GreetingServiceImpl, MusicServiceImpl, ValuesServiceImpl}
import ru.otus.sc.service.{EchoService, GreetingService, MusicService, ValuesService}

/**
 * App for using the application
 */
trait App {
  /**
   * Greet user by name. Panic if user is not a human
   *
   * @param request GreetRequest
   * @return GreetResponse
   */
  def greet(request: GreetRequest): GreetResponse

  /**
   * Get number of method invocations
   *
   * @return Long - number of invocations
   */
  def getInvocations: Long

  /**
   * Echo the input
   *
   * @param input String
   * @return String
   */
  def echo(input: String): String

  /**
   * Get StorageValue by key
   *
   * @param key Key
   * @return StorageValue
   */
  def getByKey(key: Key): StorageValue

  /**
   * Set key value
   *
   * @param key   Key
   * @param value StorageValue
   */
  def setKey(key: Key, value: StorageValue)

  /**
   * Get all StorageValue entities
   *
   * @return List[Key]
   */
  def getAllValues: List[StorageValue]

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

  def addBand(band: Band): Boolean

  def addAlbum(bandId: UUID, album: Album): Boolean

  def addTrack(albumId: UUID, track: Track): Boolean

  def updateBand(band: Band): Boolean

  def updateAlbum(album: Album): Boolean

  def updateTrack(track: Track): Boolean

  def deleteBand(id: UUID): Boolean

  def deleteAlbum(id: UUID): Boolean

  def deleteTrack(id: UUID): Boolean

  def getBandByName(name: String): Option[Band]

  def getAlbumByName(name: String): Option[Album]

  def getTracksByAlbumName(name: String): List[Track]
}

object App {
  private var invocationCounter = 0L

  private class AppCountingDecorator(app: App) extends App {

    override def greet(request: GreetRequest): GreetResponse = {
      invocationCounter += 1
      app.greet(request)
    }

    override def getInvocations: Long = invocationCounter

    override def echo(input: String): String = {
      invocationCounter += 1
      app.echo(input)
    }

    override def getByKey(key: Key): StorageValue = {
      invocationCounter += 1
      app.getByKey(key)
    }

    override def setKey(key: Key, value: StorageValue): Unit = {
      invocationCounter += 1
      app.setKey(key, value)
    }

    override def getAllValues: List[StorageValue] = {
      invocationCounter += 1
      app.getAllValues
    }

    override def getAllAlbums: List[Album] = {
      invocationCounter += 1
      app.getAllAlbums
    }

    override def getTracksByBandName(name: String): List[Track] = {
      invocationCounter += 1
      app.getTracksByBandName(name)
    }

    override def getTracksBySingerName(name: String): List[Track] = {
      invocationCounter += 1
      app.getTracksBySingerName(name)
    }

    override def addBand(band: Band): Boolean = {
      invocationCounter += 1
      app.addBand(band)
    }

    override def addAlbum(bandId: UUID, album: Album): Boolean = {
      invocationCounter += 1
      app.addAlbum(bandId, album)
    }

    override def addTrack(albumId: UUID, track: Track): Boolean = {
      invocationCounter += 1
      app.addTrack(albumId, track)
    }

    override def updateBand(band: Band): Boolean = {
      invocationCounter += 1
      app.updateBand(band)
    }

    override def updateAlbum(album: Album): Boolean = {
      invocationCounter += 1
      app.updateAlbum(album)
    }

    override def updateTrack(track: Track): Boolean = {
      invocationCounter += 1
      app.updateTrack(track)
    }

    override def deleteBand(id: UUID): Boolean = {
      invocationCounter += 1
      app.deleteBand(id)
    }

    override def deleteAlbum(id: UUID): Boolean = {
      invocationCounter += 1
      app.deleteAlbum(id)
    }

    override def deleteTrack(id: UUID): Boolean = {
      invocationCounter += 1
      app.deleteTrack(id)
    }

    override def getBandByName(name: String): Option[Band] = {
      invocationCounter += 1
      app.getBandByName(name)
    }

    override def getAlbumByName(name: String): Option[Album] = {
      invocationCounter += 1
      app.getAlbumByName(name)
    }

    override def getTracksByAlbumName(name: String): List[Track] = {
      invocationCounter += 1
      app.getTracksByAlbumName(name)
    }
  }

  private class AppImpl(greeting: GreetingService, echo: EchoService, valuesService: ValuesService, musicService: MusicService) extends App {
    override def greet(request: GreetRequest): GreetResponse = greeting.greet(request)

    override def getInvocations: Long = invocationCounter;

    override def echo(input: String): String = echo.echo(input)

    override def getByKey(key: Key): StorageValue = valuesService.getByKey(key)

    override def setKey(key: Key, value: StorageValue): Unit = valuesService.setKey(key, value)

    override def getAllValues: List[StorageValue] = valuesService.getAllValues

    override def getAllAlbums: List[Album] = musicService.getAllAlbums

    override def getTracksByBandName(name: String): List[Track] = musicService.getTracksByBandName(name)

    override def getTracksBySingerName(name: String): List[Track] = musicService.getTracksBySingerName(name)

    override def addBand(band: Band): Boolean = musicService.addBand(band)

    override def addAlbum(bandId: UUID, album: Album): Boolean = musicService.addAlbum(bandId, album)

    override def addTrack(albumId: UUID, track: Track): Boolean = musicService.addTrack(albumId, track)

    override def updateBand(band: Band): Boolean = musicService.updateBand(band)

    override def updateAlbum(album: Album): Boolean = musicService.updateAlbum(album)

    override def updateTrack(track: Track): Boolean = musicService.updateTrack(track)

    override def deleteBand(id: UUID): Boolean = musicService.deleteBand(id)

    override def deleteAlbum(id: UUID): Boolean = musicService.deleteAlbum(id)

    override def deleteTrack(id: UUID): Boolean = musicService.deleteTrack(id)

    override def getBandByName(name: String): Option[Band] = musicService.getBandByName(name)

    override def getAlbumByName(name: String): Option[Album] = musicService.getAlbumByName(name)

    override def getTracksByAlbumName(name: String): List[Track] = musicService.getTracksByAlbumName(name)
  }

  def apply(): App = {
    val greetingDao = new GreetingDaoImpl
    val valueDao = new StorageValueDaoImpl()
    val albumDao = new AlbumDaoImpl()
    val bandDao = new BandDaoImpl()
    val trackDao = new TrackDaoImpl()
    val greetingService = new GreetingServiceImpl(greetingDao)
    val echoService = new EchoServiceImpl()
    val valuesService = new ValuesServiceImpl(valueDao)
    val musicService = new MusicServiceImpl(albumDao, bandDao, trackDao)
    new AppCountingDecorator(new AppImpl(greetingService, echoService, valuesService, musicService))
  }
}
