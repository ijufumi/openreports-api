package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.service.settings.{
  ReportParamSettingsService,
  ReportSettingsService,
  ReportTemplateSettingsService
}
import skinny.controller.Params
import skinny.validator.{longValue, maxLength, paramKey, required}

class ReportSettingsController extends ApplicationController {

  override val activeMenu = "settings/report"
  override val requiredMemberInfo = true

  val path = RootPath + "/report"
  val viewPath = RootPath + "/report"

  def requestParams = Params(params)

  def validateRegisterParams = validation(
    requestParams,
    paramKey("reportName") is required & maxLength(250),
    paramKey("description") is maxLength(250),
    paramKey("templateId") is required
  )

  def validateUpdateParams = validation(
    requestParams,
    paramKey("reportName") is required & maxLength(250),
    paramKey("description") is maxLength(250),
    paramKey("templateId") is required,
    paramKey("versions") is required & longValue
  )

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

  def doRegister = {
    if (validateRegisterParams.validate) {
      val reportName = params.getAs[String]("reportName").get
      val description = params.getAs[String]("description").getOrElse("")
      val templateId = params.getAs[Long]("templateId").getOrElse(0L)

      val statusCode = new ReportSettingsService().registerReport(
        reportName,
        description,
        templateId
      )

      statusCode match {
        case StatusCode.OK => redirect(url(Controllers.reportSettings.registerCompletedUrl))
        case StatusCode.DUPLICATE_ERR =>
          set("customErrorMessages", Seq(i18n.get("warning.duplicateRegister")))
        case _ =>
          set("customErrorMessages", Seq(i18n.get("error.systemError")))
      }
    } else {
      logger.info("invalid params:%s".format(params))
    }
    val templates = new ReportTemplateSettingsService().getReportTemplates
    set("templates", templates)
    render(viewPath + "/register")
  }

  def registerCompleted = {
    render(viewPath + "/register-completed")
  }

  def showUpdate = {
    params.getAs[Long]("id").map { id =>
      val reportOpt = new ReportSettingsService().getReport(id)
      if (reportOpt.isEmpty) {
        haltWithBody(404)
      }
      val templates = new ReportTemplateSettingsService().getReportTemplates
      set("templates", templates)
      set("report", reportOpt.get)
      render(viewPath + "/update")
    } getOrElse haltWithBody(404)
  }

  def doUpdate = {
    params.getAs[Long]("id").map { id =>
      if (validateUpdateParams.validate) {
        val reportName = params.getAs[String]("reportName").get
        val description = params.getAs[String]("description").getOrElse("")
        val templateId = params.getAs[Long]("templateId").getOrElse(0L)
        val versions = params.getAs[Long]("versions").getOrElse(0L)

        val statusCode = new ReportSettingsService().updateReport(
          id,
          reportName,
          description,
          templateId,
          versions
        )

        logger.info("updateReport result:%s".format(statusCode))

        statusCode match {
          case StatusCode.OK => redirect(url(Controllers.reportSettings.updateCompletedUrl))
          case StatusCode.DUPLICATE_ERR =>
            set("customErrorMessages", Seq(i18n.get("warning.duplicateRegister")))
          case _ =>
            set("customErrorMessages", Seq(i18n.get("error.systemError")))
        }
      } else {
        logger.info("invalid params:%s".format(params))
      }
      val reportOpt = new ReportSettingsService().getReport(id)
      if (reportOpt.isEmpty) {
        haltWithBody(404)
      }
      set("report", reportOpt.get)
      val templates = new ReportTemplateSettingsService().getReportTemplates
      set("templates", templates)
      render(viewPath + "/update")
    } getOrElse haltWithBody(404)
  }

  def updateCompleted = {
    render(viewPath + "/update-completed")
  }
}
