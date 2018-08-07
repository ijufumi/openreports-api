package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import skinny.controller.Params

class ReportParamSettingsController extends ApplicationController {

  val path = rootPath + "/report-param"
  val viewPath = rootPath + "/report-param"

  def index = {
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

  def requestParams = Params(params)
}
