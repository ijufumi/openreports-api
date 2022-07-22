package jp.ijufumi.openreports.services.impl

import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.cache.{CacheKeys, CacheWrapper}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.services.GoogleService
import jp.ijufumi.openreports.utils.Strings

import scala.collection.mutable

@Singleton
class GoogleServiceImpl @Inject() (cacheWrapper: CacheWrapper) extends GoogleService {
  private val OAUTH_URL = "https://accounts.google.com/o/oauth2/auth"
  private val TOKEN_URL = "https://oauth2.googleapis.com/token"
  private val USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo"
  private val SCOPES = Array("profile", "email")

  override def getAuthorizationUrl(): String = {
    val state = Strings.generateRandomSting(10)
    cacheWrapper.put(CacheKeys.GoogleAuthState, state)

    val params = mutable.Map[String, Any]()
    params + ("client_id" -> Config.GOOGLE_CLIENT_ID)
    params + ("response_type" -> "code")
    params + ("state" -> state)
    params + ("scopes" -> SCOPES.mkString(","))
    s"${OAUTH_URL}?${Strings.convertFromMap(params)}"
  }

  override def fetchToken(code: String): String = ???

  override def getUserInfo(accessToken: String): Unit = ???
}
