package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.service.settings.GroupSettingsService
import skinny.Params
import skinny.validator.{longValue, maxLength, paramKey, required}

class GroupSettingsController extends ApplicationController {
  override val activeMenu = "settings/group"
  override val requiredMemberInfo = true
  val path = rootPath + "/group"
  val viewPath = rootPath + "/group"

  def index = {
    val groups = new GroupSettingsService().getGroups()
    set("groups", groups)
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

  def doRegister = {
    if (validateRegisterParams.validate) {
      val groupName = params.getAs[String]("groupName").getOrElse("")
      val statusCode = new GroupSettingsService().registerGroup(groupName)

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

  def validateRegisterParams = validation(
    requestParams,
    paramKey("groupName") is required & maxLength(250)
  )

  def registerCompleted = {
    render(viewPath + "/register-completed")
  }

  def showUpdate = {
    if (validateUpdateParams.validate) {
      params.getAs[Long]("id").map { id =>
        val groupOpt = new GroupSettingsService().getGroup(id)
        if (groupOpt.isEmpty) {
          haltWithBody(404)
        } else {
          set("group", groupOpt.get)
          render(viewPath + "/update")
        }
      } getOrElse haltWithBody(404)
    } else {
      logger.info("invalid params:%s".format(params))
      render(viewPath + "/update")
    }
  }

  def validateUpdateParams = validation(
    requestParams,
    paramKey("groupName") is required & maxLength(250),
    paramKey("versions") is required & longValue
  )

  def requestParams = Params(params)

  def doUpdate =
    params.getAs[Long]("id").map { id =>
      val groupName = params.getAs[String]("groupName").getOrElse("")
      val versions = params.getAs[Long]("versions").getOrElse(0)

      val statusCode =
        new GroupSettingsService().updateGroup(id, groupName, versions)
      statusCode match {
        case StatusCode.OK => redirect(path + "/updateCompleted")
        case _ =>
          set("customErrorMessages", Seq(i18n.get("error.systemError")))
      }

      render(viewPath + "/update")

    } getOrElse haltWithBody(404)

  def updateCompleted = {
    render(viewPath + "/update-completed")
  }
}
