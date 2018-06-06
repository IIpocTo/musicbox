package musicbox

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

object Server extends App with StrictLogging {

  implicit val system: ActorSystem = ActorSystem("musicbox-server-system")
  implicit def executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()

  def config: Config = ConfigFactory.load()

  private val mainRouter = new HttpRoute()
  private val interface = config.getString("http.interface")
  private val port = config.getInt("http.port")

  val routesLogged =
    DebuggingDirectives.logRequestResult("Musicbox Frontend", Logging.InfoLevel)(mainRouter.routes)

  val bindingFuture = Http().bindAndHandle(routesLogged, interface, port)
  logger.info(s"Server online at http://$interface:$port/")

}
