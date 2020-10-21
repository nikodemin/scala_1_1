import java.time.LocalDate
import java.util.UUID

import ru.otus.sc.App
import ru.otus.sc.model.dto.GreetRequest
import ru.otus.sc.model.entity.{Album, Band, StorageValue, Track}
import ru.otus.sc.model.enums.Key

object Main {
  def main(args: Array[String]): Unit = {
    val app = App()
    val trackUuid = UUID.randomUUID()

    List(
      app.greet(GreetRequest("Nikita")).greeting,

      app.getAllValues,
      app.getByKey(Key.b),
      app.setKey(Key.c, new StorageValue("Bob", 99)),
      app.getAllValues,

      app.getTracksByBandName("Papa Roach"),
      app.getAllAlbums,
      app.getTracksBySingerName("Anthony Kiedis"),
      app.getInvocations,
      app.addBand(Band("band1", "singer1", LocalDate.now(), List.empty)),
      app.addAlbum(app.getBandByName("band1").get.id, Album("album1", LocalDate.now(), List.empty, null)),
      app.addAlbum(app.getBandByName("band1").get.id, Album("album2", LocalDate.now(), List.empty, null)),
      app.addTrack(app.getAlbumByName("album1").get.id, Track("track1", 3.2f, null)),
      app.addTrack(app.getAlbumByName("album1").get.id, Track("track2", 3.2f, null, trackUuid)),
      app.updateAlbum(app.getAlbumByName("album1").get.copy(name = "new album")),
      app.getTracksByAlbumName("new album"),
      app.deleteTrack(trackUuid),
      app.getTracksByAlbumName("new album")
    ).foreach(println)
  }
}
