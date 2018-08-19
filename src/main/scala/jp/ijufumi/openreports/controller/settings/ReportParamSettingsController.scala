package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.enums.{ReportParamType, StatusCode}
import jp.ijufumi.openreports.service.settings.{
  MemberSettingsService,
  ReportParamSettingsService
}
import skinny.Params
import skinny.validator.{maxLength, numeric, paramKey, required}

class ReportParamSettingsController extends ApplicationController {

  val path = RootPath + "/report-param"
  val viewPath = RootPath + "/report-param"

  def requestParams = Params(params)

  def validateRegisterParams = validation(
    requestParams,
    paramKey("paramKey") is required & maxLength(250),
    paramKey("paramName") is required & maxLength(250),
    paramKey("description") is maxLength(250),
    paramKey("paramType") is required
  )

  def validateUpdateParams = validation(
    requestParams,
    paramKey("paramKey") is required & maxLength(250),
    paramKey("paramName") is required & maxLength(250),
    paramKey("description") is maxLength(250),
    paramKey("paramType") is required,
    paramKey("versions") is required & numeric
  )

  def index = {
    val params = new ReportParamSettingsService().getParams
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
        paramKey,
        paramName,
        description,
        paramType
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

  def doUpdate = {
    params.getAs[Long]("id").map { id =>
      if (validateUpdateParams.validate) {
        val paramKey = params.getAs[String]("paramKey").getOrElse("")
        val paramName = params.getAs[String]("paramName").getOrElse("")
        val description = params.getAs[String]("description").getOrElse("")
        val paramType = params.getAs[String]("paramType").getOrElse("")
        val versions = params.getAs[Long]("versions").get

        val statusCode = new ReportParamSettingsService().updateParam(
          id,
          paramKey,
          paramName,
          description,
          paramType,
          versions
        )
        statusCode match {
          case StatusCode.OK => redirect(path + "/updateCompleted")
          case _ =>
            set("customErrorMessages", Seq(i18n.get("error.systemError")))
        }
        render(viewPath + "/update/" + id)
      } else {
        logger.info("invalid params:%s".format(params))
        render(viewPath + "/update/" + id)
      }
    } getOrElse haltWithBody(404)
  }

  def updateCompleted = {
    render(viewPath + "/update-completed")
  }
}
