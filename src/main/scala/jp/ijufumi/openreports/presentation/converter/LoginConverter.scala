package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.presentation.request.{Login, GoogleLogin}
import jp.ijufumi.openreports.usecase.port.input.param.{LoginInput, GoogleLoginInput}

object LoginConverter {
  def toLoginInput(request: Login): LoginInput = {
    LoginInput(request.email, request.password)
  }

  def toGoogleLoginInput(request: GoogleLogin): GoogleLoginInput = {
    GoogleLoginInput(request.code)
  }
}
