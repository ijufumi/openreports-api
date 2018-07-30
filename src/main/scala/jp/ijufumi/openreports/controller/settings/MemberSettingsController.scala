package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.MemberSettingsService
import jp.ijufumi.openreports.service.enums.StatusCode
import skinny.Params
import skinny.validator.{email, numeric, paramKey, required}

class MemberSettingsController extends ApplicationController {
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

  def validateUpdateParams = validation(
    requestParams,
    paramKey("versions") is required & numeric,
    paramKey("emailAddress") is email
  )

  def index = {
    val members = new MemberSettingsService().getMembers()
    set("members", members)
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

  def doRegister = {
    if (validateRegisterParams.validate) {
      val password = params.getAs[String]("password").getOrElse("")
      val checkedPassword =
        params.getAs[String]("checkedPassword").getOrElse("")
      // パスワードの不一致
      if (!password.equals(checkedPassword)) {
        logger.info("invalid params:%s".format(params))
        set("customErrorMessages", Seq(i18n.get("warning.passwordMismatch")))
        render(viewPath + "/register")
      }
      val name = params.getAs[String]("name").getOrElse("")
      val emailAddress =
        params.getAs[String]("emailAddress").getOrElse("")
      val isAdmin = params.getAs[Boolean]("isAdmin").getOrElse(false)
      val statusCode = new MemberSettingsService()
        .registerMember(
          name = name,
          emailAddress = emailAddress,
          password = password,
          isAdmin = isAdmin
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

  def showUpdate =
    params.getAs[Long]("id").map { id =>
      val memberOpt = new MemberSettingsService().getMember(id)
      if (memberOpt.isEmpty) {
        haltWithBody(404)
      } else {
        set("member", memberOpt.get)
        render(viewPath + "/update")
      }
    } getOrElse haltWithBody(404)

  def doUpdate =
    params.getAs[Long]("id").map { id =>
      logger.info("[%d]params:%s".format(id, params))

      if (validateUpdateParams.validate) {
        val password = params.getAs[String]("password").getOrElse("")
        val checkedPassword =
          params.getAs[String]("checkedPassword").getOrElse("")
        // パスワードの不一致
        if (!password.equals(checkedPassword)) {
          logger.info("invalid params:%s".format(params))
          set("customErrorMessages", Seq(i18n.get("warning.passwordMismatch")))
          render(viewPath + "/update")
        }
        val name = params.getAs[String]("name").getOrElse("")
        val emailAddress =
          params.getAs[String]("emailAddress").getOrElse("")
        val isAdmin = params.getAs[Boolean]("isAdmin").getOrElse(false)
        val versions = params.getAs[Long]("versions").get

        val statusCode = new MemberSettingsService()
          .updateMember(id, name, emailAddress, password, isAdmin, versions)

        statusCode match {
          case StatusCode.OK => redirect(path + "/updateCompleted")
          case _ =>
            set("customErrorMessages", Seq(i18n.get("error.systemError")))
        }
        render(viewPath + "/update")
      } else {
        logger.info("invalid params:%s".format(params))
        render(viewPath + "/update")
      }
    } getOrElse haltWithBody(404)

  def updateCompleted = {
    render(viewPath + "/update-completed")
  }
}
