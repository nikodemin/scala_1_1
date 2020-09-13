import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import org.flywaydb.core.Flyway
import ru.otus.sc.config.DbConfig
import ru.otus.sc.dao.impl.{AlbumDaoImpl, BandDaoImpl, TrackDaoImpl}
import ru.otus.sc.route.{AlbumRouter, BandRouter, MusicRouterGuard, TrackRouter}
import ru.otus.sc.service.impl.{AlbumServiceImpl, BandServiceImpl, TrackServiceImpl}
import slick.jdbc.JdbcBackend.Database

import scala.io.StdIn
import scala.util.Using

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system")

    import system.dispatcher

    val config = DbConfig.default

    Using.resource(Database.forURL(config.dbUrl, config.dbUserName, config.dbPassword)) { db =>

      Flyway
        .configure()
        .dataSource(config.dbUrl, config.dbUserName, config.dbPassword)
        .load()
        .migrate()

      val albumDao = new AlbumDaoImpl(db)
      val bandDao = new BandDaoImpl(db)
      val trackDao = new TrackDaoImpl(db)

      val bandService = new BandServiceImpl(bandDao)
      val albumService = new AlbumServiceImpl(albumDao)
      val trackService = new TrackServiceImpl(trackDao)

      val bandRouter = new BandRouter(bandService)
      val albumRouter = new AlbumRouter(albumService)
      val trackRouter = new TrackRouter(trackService)

      val musicRouter = new MusicRouterGuard(List(bandRouter, albumRouter, trackRouter))

      val binding = Http().newServerAt("localhost", 8080).bind(musicRouter.route)

      binding.foreach(b => println(s"Binding on ${b.localAddress}"))

      StdIn.readLine()

      binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
    }
  }
}
