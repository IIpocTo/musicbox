package musicbox.service

//import java.util.concurrent.TimeUnit
//
//import com.typesafe.config.Config
//import musicbox.db.UserDao
//import musicbox.modles.Models.User
//import musicbox.routes.{SignInData, SignUpData}
//import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
//import reactivemongo.bson.BSONObjectID
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class AuthService(userDao: UserDao)(
//  implicit executionContext: ExecutionContext,
//  config: Config
//) {
//
//  def signIn(signInData: SignInData): Future[User] = {
//    for {
//      user <- userDao.findUserByUsernameAndPassword(signInData.username, signInData.password)
//      if user.isDefined
//    } yield user.get
//  }
//
//  def signUp(signUpData: SignUpData): Future[Boolean] = {
//    userDao
//      .createUser(
//        User(, signUpData.username, signUpData.password, signUpData.email, signUpData.phone)
//      )
//      .map(wr => wr.ok || wr.n == 1)
//  }
//
//  def getClaims(jwt: String): Option[Map[String, String]] = jwt match {
//    case JsonWebToken(_, claims, _) => claims.asSimpleMap.toOption
//    case _                          => None
//  }
//
//  def setClaims(username: String) = JwtClaimsSet(
//    Map(
//      "user" -> username,
//      "expiredAt" -> (System.currentTimeMillis() + TimeUnit.HOURS
//        .toMillis(config.getLong("jwt.tokenExpiryPeriodInHours")))
//    )
//  )
//
//  lazy val providedSecretKey: String = config.getString("jwt.secretKey")
//
//  def isTokenExpired(jwt: JwtClaim): Boolean = jwt match {
//    case Some(claims) =>
//      claims.get("expiredAt") match {
//        case Some(value) => value.toLong < System.currentTimeMillis()
//        case None        => false
//      }
//    case None => false
//  }
//
//}
