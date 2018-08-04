package jp.ijufumi.openreports.controller.settings
import jp.ijufumi.openreports.controller.common.I18nFeature
import jp.ijufumi.openreports.service.settings.{ReportSettingsService, ReportTemplateSettingsService}
import skinny.controller.SkinnyServlet
import skinny.controller.feature.FileUploadFeature

class ReportTemplateSettingsController
    extends SkinnyServlet
    with FileUploadFeature
    with I18nFeature {

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
