package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.service.settings.ReportGroupSettingsService
import skinny.Params
import skinny.validator.{longValue, maxLength, paramKey, required}

class ReportGroupSettingsController extends ApplicationController {
  override val activeMenu = "settings/report-group"
  override val requiredMemberInfo = true
  val path = rootPath + "/report-group"
  val viewPath = rootPath + "/report-group"

  def requestParams = Params(params)

  def validateRegisterParams = validation(
    requestParams,
    paramKey("reportGroupName") is required & maxLength(250)
  )

  def validateUpdateParams = validation(
    requestParams,
    paramKey("reportGroupName") is required & maxLength(250),
    paramKey("versions") is required & longValue
  )

  def index = {
    val groups = new ReportGroupSettingsService().getGroups
    set("groups", groups)
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

  def doRegister = {
    if (validateRegisterParams.validate) {
      val reportGroupName = params.getAs[String]("reportGroupName").get
      val statusCode =
        new ReportGroupSettingsService().registerGroup(reportGroupName)
      statusCode match {
        case StatusCode.OK => redirect(path + "/registerCompleted")
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
      val groupOpt = new ReportGroupSettingsService().getGroup(id)
      if (groupOpt.isEmpty) {
        haltWithBody(404)
      } else {
        set("group", groupOpt.get)
        render(viewPath + "/update")
      }
    } getOrElse haltWithBody(404)

  }

  def doUpdate = {
    if (validateUpdateParams.validate) {
      params.getAs[Long]("id").map { id =>
        val groupName = params.getAs[String]("groupName").getOrElse("")
        val versions = params.getAs[Long]("versions").getOrElse(0L)

        val statusCode =
          new ReportGroupSettingsService().updateGroup(id, groupName, versions)
        statusCode match {
          case StatusCode.OK => redirect(path + "/updateCompleted")
          case _ =>
            set("customErrorMessages", Seq(i18n.get("error.systemError")))
        }

        render(viewPath + "/update")
      } getOrElse haltWithBody(404)
    } else {
      logger.info("invalid params:%s".format(params))
      render(viewPath + "/update")
    }
  }

  def updateCompleted = {
    render(viewPath + "/update-completed")
  }
}
