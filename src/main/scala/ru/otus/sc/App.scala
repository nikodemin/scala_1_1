package ru.otus.sc

import ru.otus.sc.dao.impl._
import ru.otus.sc.model.dto.{GreetRequest, GreetResponse}
import ru.otus.sc.model.entity.{Album, StorageValue, Track}
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
