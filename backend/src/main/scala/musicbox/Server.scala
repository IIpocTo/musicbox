package musicbox

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor

object Server extends App with Service {
  override implicit val system: ActorSystem = ActorSystem("musicbox-server-system")
  override implicit def executor: ExecutionContextExecutor = system.dispatcher
  override implicit val materializer: Materializer = ActorMaterializer()

  override def config: Config = ConfigFactory.load()
  override val logger: LoggingAdapter = Logging(system, getClass)

  private val interface = config.getString("http.interface")
  private val port = config.getInt("http.port")

  Http().bindAndHandle(routes, interface, port)
  println(s"Server online at http://$interface:$port/")
}
