package jp.ijufumi.openreports.infrastructure.external.google

import com.google.inject.Inject
import jp.ijufumi.openreports.domain.port.{GoogleAuthPort, GoogleUserInfo}

class GoogleAuthAdapter @Inject() (googleRepository: GoogleRepository) extends GoogleAuthPort {
  override def getAuthorizationUrl(state: String): String =
    googleRepository.getAuthorizationUrl(state)

  override def fetchToken(code: String): Option[String] = googleRepository.fetchToken(code)

  override def getUserInfo(accessToken: String): Option[GoogleUserInfo] = {
    googleRepository.getUserInfo(accessToken).map { userInfo =>
      GoogleUserInfo(userInfo.id, userInfo.email, userInfo.name, userInfo.picture)
    }
  }
}
