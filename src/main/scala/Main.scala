import ru.otus.sc.App
import ru.otus.sc.model.dto.GreetRequest
import ru.otus.sc.model.entity.StorageValue
import ru.otus.sc.model.enums.Key

object Main {
  def main(args: Array[String]): Unit = {
    val app = App()
    List(
      app.greet(GreetRequest("Nikita")).greeting,
      app.getAllValues,
      app.getByKey(Key.b),
      app.setKey(Key.c, new StorageValue("Bob", 99)),
      app.getAllValues,
      app.getTracksByBandName("Papa Roach"),
      app.getAllAlbums,
      app.getTracksBySingerName("Anthony Kiedis"),
      app.getInvocations
    ).foreach(println)
  }
}
