package musicbox
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.Http
//import akka.stream.{ActorMaterializer, Materializer}
//import com.typesafe.config.{Config, ConfigFactory}
//
//import scala.concurrent.ExecutionContextExecutor
//
//object Server extends App {
//
//  implicit val system: ActorSystem = ActorSystem("musicbox-server-system")
//  implicit def executor: ExecutionContextExecutor = system.dispatcher
//  implicit val materializer: Materializer = ActorMaterializer()
//
//  def config: Config = ConfigFactory.load()
//
//  private val mainRouter = new HttpRoute()
//  private val interface = config.getString("http.interface")
//  private val port = config.getInt("http.port")
//
//  Http().bindAndHandle(mainRouter.routes, interface, port)
//  println(s"Server online at http://$interface:$port/")
//}
