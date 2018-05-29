package musicbox.session

import java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}

case class CookieConfig(
  name: String,
  domain: Option[String],
  path: Option[String],
  secure: Boolean,
  httpOnly: Boolean
)

case class HeaderConfig(sendToClientHeaderName: String, getFromClientHeaderName: String)

case class SessionConfig(
  serverSecret: String,
  sessionCookieConfig: CookieConfig,
  sessionHeaderConfig: HeaderConfig,
  sessionMaxAgeSeconds: Option[Long],
  sessionEncryptData: Boolean,
  csrfCookieConfig: CookieConfig,
  csrfSubmittedName: String,
  refreshTokenCookieConfig: CookieConfig,
  refreshTokenHeaderConfig: HeaderConfig,
  refreshTokenMaxAgeSeconds: Long,
  removeUsedRefreshTokenAfter: Long
) {
  require(serverSecret.length > 64, "Server secret must be at least 64 characters long!")
}

object SessionConfig {
  private implicit class PimpedConfig(config: Config) {
    val noneValue = "none"

    def getOptionalString(path: String): Option[String] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getString(path))

    def getOptionalLong(path: String): Option[Long] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getLong(path))

    def getOptionalDurationSeconds(path: String): Option[Long] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getDuration(path, TimeUnit.SECONDS))
  }

  def fromConfig(config: Config = ConfigFactory.load()): SessionConfig = {
    val scopedConfig = config.getConfig("akka.http.session")
    val csrfConfig = scopedConfig.getConfig("csrf")
    val refreshTokenConfig = scopedConfig.getConfig("refresh-token")

    SessionConfig(
      serverSecret = scopedConfig.getString("server-secret"),
      sessionCookieConfig = CookieConfig(
        name = scopedConfig.getString("cookie.name"),
        domain = scopedConfig.getOptionalString("cookie.domain"),
        path = scopedConfig.getOptionalString("cookie.path"),
        secure = scopedConfig.getBoolean("cookie.secure"),
        httpOnly = scopedConfig.getBoolean("cookie.http-only")
      ),
      sessionHeaderConfig = HeaderConfig(
        sendToClientHeaderName = scopedConfig.getString("header.send-to-client-name"),
        getFromClientHeaderName = scopedConfig.getString("header.get-from-client-name")
      ),
      sessionMaxAgeSeconds = scopedConfig.getOptionalDurationSeconds("max-age"),
      sessionEncryptData = scopedConfig.getBoolean("encrypt-data"),
      csrfCookieConfig = CookieConfig(
        name = csrfConfig.getString("cookie.name"),
        domain = csrfConfig.getOptionalString("cookie.domain"),
        path = csrfConfig.getOptionalString("cookie.path"),
        secure = csrfConfig.getBoolean("cookie.secure"),
        httpOnly = csrfConfig.getBoolean("cookie.http-only")
      ),
      csrfSubmittedName = csrfConfig.getString("submitted-name"),
      refreshTokenCookieConfig = CookieConfig(
        name = refreshTokenConfig.getString("cookie.name"),
        domain = refreshTokenConfig.getOptionalString("cookie.domain"),
        path = refreshTokenConfig.getOptionalString("cookie.path"),
        secure = refreshTokenConfig.getBoolean("cookie.secure"),
        httpOnly = refreshTokenConfig.getBoolean("cookie.http-only")
      ),
      refreshTokenHeaderConfig = HeaderConfig(
        sendToClientHeaderName = refreshTokenConfig.getString("header.send-to-client-name"),
        getFromClientHeaderName = refreshTokenConfig.getString("header.get-from-client-name")
      ),
      refreshTokenMaxAgeSeconds = refreshTokenConfig.getDuration("max-age", TimeUnit.SECONDS),
      removeUsedRefreshTokenAfter =
        refreshTokenConfig.getDuration("remove-used-token-after", TimeUnit.SECONDS)
    )
  }

  def default(serverSecret: String): SessionConfig =
    fromConfig(
      ConfigFactory.load
        .withValue("akka.http.session.server-secret", ConfigValueFactory.fromAnyRef(serverSecret))
    )

}
