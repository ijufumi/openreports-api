package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.settings.ReportSettingsService
import skinny.controller.Params

class ReportSettingsController extends ApplicationController {

  val path = rootPath + "/report"
  val viewPath = rootPath + "/report"

  def index = {
    val reports = new ReportSettingsService().getReports()
    set("reports", reports)
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

  def requestParams = Params(params)
}
