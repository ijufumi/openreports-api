package jp.ijufumi.openreports.services.impl

import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.cache.CacheWrapper
import jp.ijufumi.openreports.services.GoogleService

@Singleton
class GoogleServiceImpl @Inject() (cacheWrapper: CacheWrapper) extends GoogleService {
  private val OAUTH_URL = "https://accounts.google.com/o/oauth2/auth"
  private val TOKEN_URL = "https://oauth2.googleapis.com/token"
  private val USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo"
  private val SCOPES = Array("profile", "email")

  override def getAuthorizationUrl(): String = {

    ""
  }

  override def fetchToken(code: String): String = ???

  override def getUserInfo(accessToken: String): Unit = ???
}
