package musicbox

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Server extends App with StrictLogging {

  implicit val system: ActorSystem = ActorSystem("musicbox-server-system")
  implicit def executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()

  def config: Config = ConfigFactory.load()

  private val mainRouter = new HttpRoute()
  private val interface = config.getString("http.interface")
  private val port = config.getInt("http.port")

  logger.info("Server started")

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
