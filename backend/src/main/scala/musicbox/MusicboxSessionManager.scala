package musicbox

import com.typesafe.scalalogging.StrictLogging
import musicbox.jwt.{JwtSessionEncoder, JwtSessionSerializer}
import musicbox.refreshtoken.InMemoryRefreshTokenStorage
import musicbox.session.{SessionConfig, SessionSerializer}
import musicbox.session.manager.SessionManager
import org.json4s.jackson.Serialization
import org.json4s.ext.{JavaTimeSerializers, JavaTypesSerializers}
import org.json4s.{DefaultFormats, Formats, JValue, jackson}

case class SessionData(userId: String)

object MusicboxSessionManager extends StrictLogging {
  implicit val serialization: Serialization.type = jackson.Serialization
  implicit val formats: Formats =
    DefaultFormats ++ JavaTypesSerializers.all ++ JavaTimeSerializers.all
  implicit val serializer: SessionSerializer[SessionData, JValue] =
    JwtSessionSerializer.caseClass[SessionData]
  implicit val encoder: JwtSessionEncoder[SessionData] =
    new JwtSessionEncoder[SessionData]
  implicit val manager: SessionManager[SessionData] =
    new SessionManager(SessionConfig.fromConfig())
  implicit val refreshTokenStorage: InMemoryRefreshTokenStorage[SessionData] =
    (msg: String) => logger.info(msg)
}
