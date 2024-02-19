package jp.ijufumi.openreports.infrastructure.google.auth.impl

import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.infrastructure.google.auth.GoogleRepository
import jp.ijufumi.openreports.infrastructure.google.auth.models.{AccessToken, UserInfo}
import jp.ijufumi.openreports.utils.{Logging, Strings}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import sttp.client3._
import sttp.client3.json4s._
import sttp.model.Header

import scala.collection.mutable

class GoogleRepositoryImpl extends GoogleRepository with Logging {
  private implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
  private implicit val serialization: Serialization.type = org.json4s.native.Serialization

  private val OAUTH_URL = "https://accounts.google.com/o/oauth2/auth"
  private val TOKEN_URL = "https://oauth2.googleapis.com/token"
  private val USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo"
  private val REDIRECT_URL = s"${Config.FRONTEND_URL}/google/callback"
  private val SCOPES = Array("profile", "email")

  override def getAuthorizationUrl(): String = {
    val state = Strings.generateRandomSting(10)()

    val params = mutable.Map[String, Any]()
    params += ("client_id" -> Config.GOOGLE_CLIENT_ID)
    params += ("response_type" -> "code")
    params += ("state" -> state)
    params += ("scope" -> SCOPES.mkString(" "))
    params += ("redirect_uri" -> REDIRECT_URL)

    s"${OAUTH_URL}?${Strings.generateQueryParamsFromMap(params)}"
  }

  override def fetchToken(code: String): Option[String] = {
    val basicAuth =
      Strings.convertToBase64(s"${Config.GOOGLE_CLIENT_ID}:${Config.GOOGLE_CLIENT_SECRET}")

    val backend = HttpClientSyncBackend()
    val params = mutable.Map[String, Any]()
    params += ("client_id" -> Config.GOOGLE_CLIENT_ID)
    params += ("client_secret" -> Config.GOOGLE_CLIENT_SECRET)
    params += ("grant_type" -> "authorization_code")
    params += ("code" -> code)
    params += ("redirect_uri" -> REDIRECT_URL)

    val request = basicRequest
      .body(params)
      .post(uri"${TOKEN_URL}")
      .response(asJson[AccessToken])

    request.headers ++ Seq(
      Header("Authorization", s"Basic ${basicAuth}"),
      Header("Accept", "application/json"),
      Header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"),
    )

    val response = request.send(backend)
    if (!response.is200) {
      logger.warn(s"Failed to fetch token: ${response.statusText}")
      return Option.empty
    }
    val responseBody = response.body
    responseBody match {
      case Left(error) =>
        logger.warn(s"Failed to fetch token: ${error}")

        Option.empty
      case Right(accessToken) =>
        Option(accessToken.access_token)
    }
  }

  override def getUserInfo(accessToken: String): Option[UserInfo] = {
    val backend = HttpClientSyncBackend()
    val response =
      basicRequest
        .get(uri"${USER_INFO_URL}?access_token=${accessToken}")
        .response(asJson[UserInfo])
        .send(backend)

    if (response.is200) {
      return response.body.toOption
    }
    Option.empty
  }
}
