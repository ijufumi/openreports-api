package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.services.LoginService
import com.google.inject.Singleton

@Singleton
class LoginServiceImpl extends LoginService {
  override def login(email: String, password: String): Unit = {}
}
