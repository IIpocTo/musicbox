package musicbox

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging
import musicbox.refreshtoken.InMemoryRefreshTokenStorage
import musicbox.session.SessionConfig
import musicbox.session.SessionOptions._
import musicbox.session.directives.SessionDirectives._
import musicbox.session.manager.SessionManager

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Server extends App with StrictLogging {

  implicit val system: ActorSystem = ActorSystem("musicbox-server-system")
  implicit def executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()

  def config: Config = ConfigFactory.load()

  val sessionConfig = SessionConfig.default(
    "jegor3lesrinf39t7mc5h6un6r0c69lgfno69dsak3vabeqamouq4328cuaekros401ajdpkh60rrtpd8ro24rbuqmgtnd1ebag6ljnb65i8a55d482ok7o0nckarpow"
  )

  implicit val sessionManager: SessionManager[MusicboxSession] =
    new SessionManager[MusicboxSession](sessionConfig)
  implicit val refreshTokenStorage: InMemoryRefreshTokenStorage[MusicboxSession] = (msg: String) =>
    logger.info(msg)

  def musicboxSetSession(value: MusicboxSession) = setSession(refreshable, usingCookies, value)
  val musicboxRequiredSession = requiredSession(refreshable, usingCookies)
  val musicboxInvalidateSession = invalidateSession(refreshable, usingCookies)

  private val mainRouter = new HttpRoute()
  private val interface = config.getString("http.interface")
  private val port = config.getInt("http.port")

  val bindingFuture = Http().bindAndHandle(mainRouter.routes, interface, port)
  println(s"Server online at http://$interface:$port/")

  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete { _ =>
      system.terminate()
      println("Server stopped")
    }
}
