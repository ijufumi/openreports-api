package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.enums.{ReportParamType, StatusCode}
import jp.ijufumi.openreports.service.settings.{MemberSettingsService, ReportParamSettingsService}
import skinny.Params
import skinny.validator.{maxLength, paramKey, required}

class ReportParamSettingsController extends ApplicationController {

  val path = rootPath + "/report-param"
  val viewPath = rootPath + "/report-param"

  def requestParams = Params(params)

  def validateRegisterParams = validation(
    requestParams,
    paramKey("paramKey") is required & maxLength(250),
    paramKey("paramName") is required & maxLength(250),
    paramKey("description") is maxLength(250),
    paramKey("paramType") is required
  )

  def index = {
    val params = new ReportParamSettingsService().getReportParamConfig()
    set("params", params)
    render(viewPath + "/index")
  }

  def showRegister = {
    set("paramTypes", ReportParamType.list())
    render(viewPath + "/register")
  }

  def doRegister = {
    if (validateRegisterParams.validate) {
      val paramKey = params.getAs[String]("paramKey").getOrElse("")
      val paramName = params.getAs[String]("paramName").getOrElse("")
      val description = params.getAs[String]("description").getOrElse("")
      val paramType = params.getAs[String]("paramType").getOrElse("")

      val statusCode = new ReportParamSettingsService().registerParam(
        paramKey, paramName, description, paramType
      )
      statusCode match {
        case StatusCode.OK => redirect(path + "/registerCompleted")
        case StatusCode.DUPLICATE_ERR =>
          set("customErrorMessages", Seq(i18n.get("warning.duplicateRegister")))
        case _ =>
          set("customErrorMessages", Seq(i18n.get("error.systemError")))
      }
      render(viewPath + "/register")
    } else {
      logger.info("invalid params:%s".format(params))
      render(viewPath + "/register")
    }
  }

  def registerCompleted = {
    render(viewPath + "/register-completed")
  }

  def showUpdate = {
    params.getAs[Long]("id").map { id =>
      val paramOpt = new ReportParamSettingsService().getParam(id)
      if (paramOpt.isEmpty) {
        haltWithBody(404)
      } else {
        set("param", paramOpt.get)
        render(viewPath + "/update")
      }
    } getOrElse haltWithBody(404)
  }
}
