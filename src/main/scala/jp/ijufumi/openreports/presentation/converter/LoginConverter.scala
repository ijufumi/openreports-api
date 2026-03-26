package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.presentation.request.{GoogleLogin, Login}
import jp.ijufumi.openreports.usecase.port.input.param.{GoogleLoginInput, LoginInput}

object LoginConverter {
  def toLoginInput(request: Login): LoginInput = {
    LoginInput(request.email, request.password)
  }

  def toGoogleLoginInput(request: GoogleLogin): GoogleLoginInput = {
    GoogleLoginInput(request.code)
  }
}
