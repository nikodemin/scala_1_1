package ru.otus.sc.dao.impl

import java.time.LocalDate
import java.util.UUID

import ru.otus.sc.model.entity.{Album, Band, Track}

import scala.collection.mutable.ListBuffer

private object Ids {

  object Band {
    val papaRoach: UUID = UUID.randomUUID()
    val rhcp: UUID = UUID.randomUUID()
  }

  object Album {
    val gettingAwayWithMurder: UUID = UUID.randomUUID()
    val californication: UUID = UUID.randomUUID()
  }

  object Track {
    val gettingAwayWithMurder: UUID = UUID.randomUUID()
    val notListening: UUID = UUID.randomUUID()
    val aroundTheWorld: UUID = UUID.randomUUID()
    val getOnTop: UUID = UUID.randomUUID()
    val easily: UUID = UUID.randomUUID()
  }

}

package object Bands {
  private val papaRoach = Band("Papa Roach", "Jacoby Dakota Shaddix",
    LocalDate.of(1993, 1, 1), List(Ids.Album.gettingAwayWithMurder), Ids.Band.papaRoach)
  private val rhcp = Band("Red Hot Chili Peppers", "Anthony Kiedis",
    LocalDate.of(1983, 1, 1), List(Ids.Album.californication), Ids.Band.rhcp)

  val all: ListBuffer[Band] = ListBuffer(papaRoach, rhcp)
}

package object Albums {
  private val gettingAwayWithMurder = Album("Getting away with murder", LocalDate.of(2004, 7, 31),
    List(Ids.Track.gettingAwayWithMurder, Ids.Track.notListening), Ids.Band.papaRoach, Ids.Album.gettingAwayWithMurder)
  private val californication = Album("Californication", LocalDate.of(1999, 6, 8),
    List(Ids.Track.aroundTheWorld, Ids.Track.easily, Ids.Track.getOnTop), Ids.Band.rhcp, Ids.Album.californication)

  val all: ListBuffer[Album] = ListBuffer(gettingAwayWithMurder, californication)
}

package object Tracks {
  private val gettingAwayWithMurder = Track("Getting away with murder", 3.1f, Ids.Album.gettingAwayWithMurder, Ids.Track.gettingAwayWithMurder)
  private val notListening = Track("Not listening", 3.09f, Ids.Album.gettingAwayWithMurder, Ids.Track.notListening)
  private val aroundTheWorld = Track("Around the world", 3.59f, Ids.Album.californication, Ids.Track.aroundTheWorld)
  private val getOnTop = Track("Get on top", 3.18f, Ids.Album.californication, Ids.Track.getOnTop)
  private val easily = Track("Easily", 3.51f, Ids.Album.californication, Ids.Track.easily)

  val all: ListBuffer[Track] = ListBuffer(gettingAwayWithMurder, notListening, aroundTheWorld, getOnTop, easily)
}
