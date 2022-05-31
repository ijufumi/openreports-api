package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApiController
import jp.ijufumi.openreports.service.LoginService
import skinny._
import skinny.validator._

class LoginController extends ApiController {

  val path: String = PublicPath

  def login: String = {
    val loginId = requestParams.getAs("loginId").getOrElse("")
    val password = requestParams.getAs("password").getOrElse("")
    if (validateParams.validate()) {
      val memberInfoOpt = new LoginService().login(loginId, password)

      if (memberInfoOpt.isEmpty) {
        ngResponse(i18n.get("warning.loginFailure"))
      } else {
        okResponse(memberInfoOpt.get)
      }
    } else {
      ngResponse(i18n.get("warning.loginFailure"))
    }
  }

  def requestParams = Params(params)

  def validateParams: MapValidator = validation(
    requestParams,
    paramKey("loginId") is required,
    paramKey("password") is required
  )
}
