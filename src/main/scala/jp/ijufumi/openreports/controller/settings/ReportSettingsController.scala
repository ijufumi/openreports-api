package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.service.settings.{ReportParamSettingsService, ReportSettingsService, ReportTemplateSettingsService}
import jp.ijufumi.openreports.vo.ReportParamConfig
import skinny.controller.Params
import skinny.validator.{longValue, maxLength, paramKey, required}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.read

class ReportSettingsController extends ApplicationController {

  override val activeMenu = "settings/report"
  override val requiredMemberInfo = true

  val path = RootPath + "/report"
  val viewPath = RootPath + "/report"

  def requestParams = Params(params)

  def validate4Register = validation(
    requestParams,
    paramKey("reportName") is required & maxLength(250),
    paramKey("description") is maxLength(250),
    paramKey("templateId") is required
  )

  def validate4Update = validation(
    requestParams,
    paramKey("reportName") is required & maxLength(250),
    paramKey("description") is maxLength(250),
    paramKey("templateId") is required,
    paramKey("versions") is required & longValue
  )

  def validate4UpdatePrams = validation(
    requestParams,
    paramKey("params") is required,
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
    if (validate4Register.validate) {
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
      if (validate4Update.validate) {
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

  def showUpdateParams = {
    params.getAs[Long]("id").map { id =>
      val reportOpt = new ReportSettingsService().getReport(id)
      if (reportOpt.isEmpty) {
        haltWithBody(404)
      }
      val templates = new ReportTemplateSettingsService().getReportTemplates
      set("report", reportOpt.get)
      render(viewPath + "/report-param")
    } getOrElse haltWithBody(404)
  }

  def doUpdateParams = {
    params.getAs[Long]("id").map { id =>
      if (validate4UpdatePrams.validate) {
        // TODO:getting parameter values.
        val reportParams = params.getAs[String]("params").getOrElse("")
        val versions = params.getAs[Long]("versions").getOrElse(0L)
        //val reportParamsObj = parse(reportParams)
        val reportParamList = read[List[ReportParamConfig]](reportParams)

        val statusCode = new ReportSettingsService().updateReportParam(
          id,
          reportParamList,
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
      render(viewPath + "/report-param")
    } getOrElse haltWithBody(404)
  }
}
