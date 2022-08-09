package jp.ijufumi.openreports.repositories.system.impl

import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.cache.{CacheKeys, CacheWrapper}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.repositories.system.GoogleRepository
import jp.ijufumi.openreports.utils.{Logging, Strings}
import jp.ijufumi.openreports.vo.response.google.{AccessTokenResponse, UserInfoResponse}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import sttp.client3._
import sttp.client3.json4s._
import sttp.model.Header

import scala.collection.mutable

@Singleton
class GoogleRepositoryImpl @Inject() (cacheWrapper: CacheWrapper)
    extends GoogleRepository
    with Logging {
  private implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
  private implicit val serialization: Serialization.type = org.json4s.native.Serialization

  private val OAUTH_URL = "https://accounts.google.com/o/oauth2/auth"
  private val TOKEN_URL = "https://oauth2.googleapis.com/token"
  private val USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo"
  private val SCOPES = Array("profile", "email")

  override def getAuthorizationUrl(): String = {
    val state = Strings.generateRandomSting(10)
    cacheWrapper.put(CacheKeys.GoogleAuthState, state)(Config.GOOGLE_AUTH_STATE_CACHE_TTL_SEC)

    val params = mutable.Map[String, Any]()
    params += ("client_id" -> Config.GOOGLE_CLIENT_ID)
    params += ("response_type" -> "code")
    params += ("state" -> state)
    params += ("scopes" -> SCOPES.mkString(","))

    s"${OAUTH_URL}?${Strings.generateQueryParamsFromMap(params)}"
  }

  override def fetchToken(state: String, code: String): Option[String] = {
    val cachedState = cacheWrapper.get[String](CacheKeys.ApiToken)
    if (cachedState.getOrElse("") != state) {
      return Option.empty
    }
    cacheWrapper.remove(CacheKeys.ApiToken)

    val basicAuth =
      Strings.convertToBase64(s"${Config.GOOGLE_CLIENT_ID}:${Config.GOOGLE_CLIENT_SECRET}")

    val backend = HttpClientSyncBackend()
    val request = basicRequest
      .post(uri"${TOKEN_URL}?grant_type=authorization_code&code=${code}")
      .response(asJson[AccessTokenResponse])
    request.headers ++ Seq(
      Header("Authorization", s"Basic ${basicAuth}"),
      Header("Accept", "application/json"),
      Header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"),
    )

    val response = request.send(backend)
    if (response.is200) {
      val accessToken = response.body.toOption.get
      return Option(accessToken.accessToken)
    }
    Option.empty
  }

  override def getUserInfo(accessToken: String): Option[UserInfoResponse] = {
    val backend = HttpClientSyncBackend()
    val response =
      basicRequest
        .get(uri"${USER_INFO_URL}?access_token=${accessToken}")
        .response(asJson[UserInfoResponse])
        .send(backend)

    if (response.is200) {
      return response.body.toOption
    }
    Option.empty
  }
}
