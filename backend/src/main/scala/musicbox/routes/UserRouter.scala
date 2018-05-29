package musicbox.routes

//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.server.Route
//import musicbox.routes.directives.SecurityDirectives
//import musicbox.service.AuthService
//
//import scala.concurrent.ExecutionContext
//
//class UserRouter(auth: AuthService)(implicit executionContext: ExecutionContext)
//    extends SecurityDirectives {
//
//  override val authService: AuthService = auth
//
//  val route: Route =
//    pathPrefix("user") {
//      path("profile") {
//        get {
//          authenticated { claims =>
//            complete(s"User ${claims.getOrElse("user", "")} accessed secured content!")
//          }
//        }
//      }
//    }
//}
