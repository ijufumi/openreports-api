package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.service.settings.GroupSettingsService
import skinny.Params
import skinny.validator.{length, paramKey, required}

class GroupSettingController extends ApplicationController {
  val path = rootPath + "/group"
  val viewPath = rootPath + "/group"

  override val activeMenu = "settings/group"
  override val requiredMemberInfo = true

  def requestParams = Params(params)

  def validateRegisterParams = validation(
    requestParams,
    paramKey("groupName") is required & length(250)
  )

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

  def registerCompleted = {
    render(viewPath + "/register-completed")
  }
}
