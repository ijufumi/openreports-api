package jp.ijufumi.openreports.api

import org.scalatra._
import com.google.inject.Inject
import jp.ijufumi.openreports.services.LoginService

@Inject
class LoginServlet(loginService: LoginService) extends ScalatraServlet {
  get("/") {
    Ok("hello")
  }
}
