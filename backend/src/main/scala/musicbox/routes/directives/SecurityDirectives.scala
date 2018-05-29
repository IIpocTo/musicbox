package musicbox.routes.directives

//import akka.http.scaladsl.model.StatusCodes
//import akka.http.scaladsl.server.Directive1
//import akka.http.scaladsl.server.Directives._
//import authentikat.jwt.JsonWebToken
//import musicbox.service.AuthService
//
//trait SecurityDirectives {
//
//  val authService: AuthService
//
//  def authenticated: Directive1[Map[String, Any]] =
//    optionalHeaderValueByName("Authorization").flatMap {
//      case Some(jwt) if authService.isTokenExpired(jwt) =>
//        complete(StatusCodes.Unauthorized -> "Token expired.")
//      case Some(jwt) if JsonWebToken.validate(jwt, authService.provideSecretKey) =>
//        provide(authService.getClaims(jwt).getOrElse(Map.empty[String, Any]))
//      case _ => complete(StatusCodes.Unauthorized)
//    }
//
//}
