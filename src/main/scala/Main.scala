import akka.actor.typed.{ActorSystem, Scheduler}
import akka.actor.{ActorSystem => CLassicActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import org.flywaydb.core.Flyway
import ru.otus.sc.actor.GateActor
import ru.otus.sc.config.DbConfig
import ru.otus.sc.dao.impl.{AlbumDaoImpl, BandDaoImpl, TrackDaoImpl}
import ru.otus.sc.route._
import ru.otus.sc.service.impl.{AlbumServiceImpl, BandServiceImpl, TrackServiceImpl}
import slick.jdbc.JdbcBackend.Database
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.swagger.akkahttp.SwaggerAkka

import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.Using

object Main {
  def main(args: Array[String]): Unit = {
    implicit val classicSystem: CLassicActorSystem = CLassicActorSystem("classicSystem")

    import classicSystem.dispatcher

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

      implicit val askTimeout: Timeout = Timeout(5.second)
      implicit val timeoutDuration: FiniteDuration = 1.second

      val system: ActorSystem[GateActor.Message] = ActorSystem(GateActor(bandDao, albumDao, trackDao), "system")
      implicit val scheduler: Scheduler = system.scheduler

      val musicRouter = new MusicRouterGuard(List(albumRouter, bandRouter, trackRouter), albumRouter.endpoints ++
        bandRouter.endpoints ++ trackRouter.endpoints)
      val searchRouter = new SearchRouter(system)

      val openApiYaml = (musicRouter.endpoints ++ searchRouter.endpoints)
        .toOpenAPI("Music db", "1.0.0").toYaml

      val binding = Http().newServerAt("localhost", 8080).bind(musicRouter.route ~
        (new SwaggerAkka(openApiYaml)).routes ~ searchRouter.route)

      binding.foreach(b => println(s"Binding on ${b.localAddress}"))

      StdIn.readLine()

      binding.flatMap(_.unbind()).onComplete(_ => {
        classicSystem.terminate
        system.terminate
      })
    }
  }
}
