import cats.syntax.semigroupk._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import org.http4s.syntax.kleisli._
import ru.otus.sc.dao.AlbumDao.AlbumDao
import ru.otus.sc.dao.BandDao.BandDao
import ru.otus.sc.dao.TrackDao.TrackDao
import ru.otus.sc.dao.{AlbumDao, BandDao, TrackDao}
import ru.otus.sc.db.DbProvider.Db
import ru.otus.sc.db.Migrations.Migrations
import ru.otus.sc.db.{DbConfig, DbProvider, Migrations}
import ru.otus.sc.route.interfaces.MusicRouter
import ru.otus.sc.route.interfaces.MusicRouter.{AlbumRouter, BandRouter, ServerConfig, TrackRouter}
import ru.otus.sc.route.{AlbumRouterImpl, BandRouterImpl, TrackRouterImpl}
import ru.otus.sc.service.AlbumService.AlbumService
import ru.otus.sc.service.BandService.BandService
import ru.otus.sc.service.TrackService.TrackService
import ru.otus.sc.service.{AlbumService, BandService, TrackService}
import sttp.tapir.server.http4s.Http4sServerOptions
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.{Task, _}

object Main extends zio.App {

  type Dbs = Has[DbConfig] with Db with Migrations
  type Daos = AlbumDao with BandDao with TrackDao
  type Services = AlbumService with BandService with TrackService
  type Routers = BandRouter with AlbumRouter with TrackRouter
  type Http4Server = Has[Server[Task]]

  def createHttp4Server: RManaged[ZEnv with Routers, Server[Task]] =
    ZManaged.runtime[ZEnv with Routers].flatMap { implicit runtime: Runtime[ZEnv with Routers] =>
      BlazeServerBuilder[Task](runtime.platform.executor.asEC)
        .bindHttp(8080, "localhost")
        .withHttpApp(Router("/" ->
          (runtime.environment.get[AlbumRouterImpl].route <+>
            runtime.environment.get[BandRouterImpl].route <+>
            runtime.environment.get[TrackRouterImpl].route)
        ).orNotFound)
        .resource
        .toManagedZIO
    }

  def createHttp4sLayer: RLayer[ZEnv with Routers, Http4Server] = ZLayer.fromManaged(createHttp4Server)

  val db: RLayer[Any, Dbs] = ZIO.succeed(DbConfig.default).toLayer >+> DbProvider.live >+> Migrations.live
  val dao: RLayer[Dbs, Daos] = AlbumDao.live ++ BandDao.live ++ TrackDao.live
  val service: RLayer[Daos, Services] = AlbumService.live ++ BandService.live ++ TrackService.live
  val serverConfig: RLayer[Services, ServerConfig] = ZLayer.fromFunction(_ => Http4sServerOptions.default)
  val router: RLayer[Services with ServerConfig, Routers] = MusicRouter.albumRouterLive ++ MusicRouter.bandRouterLive ++
    MusicRouter.trackRouterLive

  val total = db >+> dao >+> service >+> serverConfig >+> router ++ ZEnv.live >+> createHttp4sLayer

  val zio: ZIO[ZEnv with Migrations, Nothing, Unit] = for {
    migrations <- ZIO.access[Migrations](_.get)
    _ <- migrations.applyMigrations()
    _ <- ZIO.never
  } yield ()

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = zio.provideLayer(total).exitCode
}

