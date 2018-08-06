package jp.ijufumi.openreports.controller.settings
import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.settings.ReportTemplateSettingsService

class ReportTemplateSettingsController extends ApplicationController {

  val path = rootPath + "/report-template"
  val viewPath = rootPath + "/report-template"

  def index = {
    val templates = new ReportTemplateSettingsService().getReportTemplates()
    set("templates", templates)
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

}
