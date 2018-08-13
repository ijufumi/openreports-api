package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.settings.{
  ReportParamSettingsService,
  ReportSettingsService,
  ReportTemplateSettingsService
}
import skinny.controller.Params

class ReportSettingsController extends ApplicationController {

  val path = rootPath + "/report"
  val viewPath = rootPath + "/report"

  def requestParams = Params(params)

  def index = {
    val reports = new ReportSettingsService().getReports
    set("reports", reports)
    render(viewPath + "/index")
  }

  def showRegister = {
    val templates = new ReportTemplateSettingsService().getReportTemplates
    val params = new ReportParamSettingsService().getParams

    set("templates", templates)
    set("params", params)
    render(viewPath + "/register")
  }
}
