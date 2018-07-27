package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.SettingMemberService
import skinny.Params
import skinny.validator.{email, paramKey, required}

class MemberSettingsController
  extends ApplicationController {
  val path = rootPath + "/member"
  val viewPath = rootPath + "/member"

  override val activeMenu = "settings/member"
  override val requiredMemberInfo = true

  def requestParams = Params(params)

  def validateRegisterParams = validation(
    requestParams,
    paramKey("name") is required,
    paramKey("emailAddress") is required & email,
    paramKey("password") is required,
    paramKey("checkedPassword") is required
  )

  def index = {
    val members = new SettingMemberService().getMembers()
    set("members", members)
    render(viewPath + "/index")
  }

  def register = {
    render(viewPath + "/register")
  }

  def register2 = {
    if (validateRegisterParams.validate) {
      val password = requestParams.getAs[String]("password").getOrElse("")
      val checkedPassword = requestParams.getAs[String]("checkedPassword").getOrElse("")
      // パスワードの不一致
      if (!password.equals(checkedPassword)) {
        logger.info("invalid params:%s".format(requestParams))
        set("customErrorMessages", Seq(i18n.get("warning.loginFailure"))) // TODO:メッセージ変更
        render(viewPath + "/register")
      }
      val name = requestParams.getAs[String]("name").getOrElse("")
      val emailAddress = requestParams.getAs[String]("emailAddress").getOrElse("")
      val isAdmin = requestParams.getAs[Boolean]("isAdmin").getOrElse(false)
      new SettingMemberService()
        .registerMember(name = name, emailAddress = emailAddress, password = password, isAdmin = isAdmin)
      // TODO:emailAddressの重複チェックとエラー処理の追加
      redirect(path + "/registerCompleted")
    } else {
      logger.info("invalid params:%s".format(requestParams))
      render(viewPath + "/register")
    }
  }

  def registerCompleted = {
    render(viewPath + "/register-complete")
  }

  def update = {
    render(viewPath + "/update")
  }

  def update2 = {
    redirect(path + "/updateCompleted")
  }

  def updateCompleted = {
    render(viewPath + "/update-complete")
  }
}
