package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.settings.ReportParamSettingsService
import skinny.controller.Params

class ReportParamSettingsController extends ApplicationController {

  val path = rootPath + "/report-param"
  val viewPath = rootPath + "/report-param"

  def index = {
    val params = new ReportParamSettingsService().getReportParamConfig()
    set("params", params)
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

  def requestParams = Params(params)
}
