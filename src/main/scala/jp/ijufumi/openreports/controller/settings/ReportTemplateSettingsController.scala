package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.{ApplicationController, I18nFeature}
import jp.ijufumi.openreports.service.settings.ReportTemplateSettingsService
import skinny.SkinnyServlet
import skinny.controller.feature.{FileUploadFeature, ThymeleafTemplateEngineFeature}
import skinny.filter.{ErrorPageFilter, SkinnySessionFilter}

class ReportTemplateSettingsController
  extends SkinnyServlet
    with SkinnySessionFilter
    with ErrorPageFilter
    with I18nFeature
    with ThymeleafTemplateEngineFeature
    with FileUploadFeature {

  val path = rootPath + "/report-template"
  val viewPath = rootPath + "/report-template"

  def index = {
    val templates = new ReportTemplateSettingsService()
      .getReportTemplates
    set("templates", templates)
    render(viewPath + "/index")
  }

  def showUpload = {
    render(viewPath + "/upload")
  }

  def upload = {
    fileParams
      .get("uploadFile") match {
      case Some(file) =>
        logger
          .info(new String(file
            .get()))
        new ReportTemplateSettingsService()
          .uploadFile(file)
      case None => logger
        .info("file not found")
    }

    redirect(url(Controllers.reportTemplateSettings.showUploadUrl))
  }
}
