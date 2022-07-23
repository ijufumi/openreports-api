package jp.ijufumi.openreports.services.impl

import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.cache.{CacheKeys, CacheWrapper}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.services.GoogleService
import jp.ijufumi.openreports.vo.response.google.AccessTokenResponse
import jp.ijufumi.openreports.utils.Strings
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import sttp.client3._
import sttp.client3.json4s._
import sttp.model.Header

import scala.collection.mutable

@Singleton
class GoogleServiceImpl @Inject() (cacheWrapper: CacheWrapper) extends GoogleService {
  private implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
  private implicit val serialization: Serialization.type = org.json4s.native.Serialization

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

  override def fetchToken(code: String): String = {
    val basicAuth =
      Strings.convertToBase64(s"${Config.GOOGLE_CLIENT_ID}:${Config.GOOGLE_CLIENT_SECRET}")

    val backend = HttpClientSyncBackend()
    val request = basicRequest
      .post(uri"${TOKEN_URL}?grant_type=authorization_code&code=${code}")
      .response(asJson[AccessTokenResponse])
    request.headers :+ Header("Authorization", s"Basic ${basicAuth}")
    request.headers :+ Header("Accept", "application/json")
    request.headers :+ Header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")

    val response = request.send(backend)
    if (response.is200) {
      val accessToken = response.body.toOption.get
      return accessToken.accessToken
    }
    ""
  }

  override def getUserInfo(accessToken: String): Unit = ???
}
