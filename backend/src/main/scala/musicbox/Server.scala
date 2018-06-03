package musicbox

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import musicbox.session.SessionConfig

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Server extends App {

  implicit val system: ActorSystem = ActorSystem("musicbox-server-system")
  implicit def executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()

  def config: Config = ConfigFactory.load()

  val sessionConfig = SessionConfig.default(
    "jegor3lesrinf39t7mc5h6un6r0c69lgfno69dsak3vabeqamouq4328cuaekros401ajdpkh60rrtpd8ro24rbuqmgtnd1ebag6ljnb65i8a55d482ok7o0nckarpow"
  )

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
