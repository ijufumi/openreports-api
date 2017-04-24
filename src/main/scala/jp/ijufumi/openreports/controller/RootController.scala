package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.model.Member
import skinny._
import skinny.controller.feature.ThymeleafTemplateEngineFeature
import skinny.validator.{ required, _ }

class RootController extends ApplicationController
    with ThymeleafTemplateEngineFeature {

  def requestParams = Params(params)

  def validateParams = validation(
    requestParams,
    paramKey("userName") is required,
    paramKey("password") is required
  )

  def index = {
    //render("/root/index")
    val memberInfo: Option[Any] = skinnySession.getAttribute("memberInfo")
    if (memberInfo.isDefined) {
      redirect("/home/")
    } else {
      render("/root/index")
    }
  }

  def login = {
    val userName = requestParams.getAs("userName").getOrElse("")
    val password = requestParams.getAs("password").getOrElse("")
    if (validateParams.validate) {
      val members: Seq[Member] = Member.where('emailAddress -> userName, 'password -> password).apply();

      if (members.isEmpty) {
        logger.info("invalid id or password : [" + userName + "][" + password + "]")
        set("userName", requestParams.getAs("userName").getOrElse(""))
        // TODO ログインエラーメッセージの設定
        render("/root/index")
      } else {
        skinnySession.setAttribute("memberInfo", userName);
        redirect("/home/")
      }
    } else {
      logger.info("invalid id or password : [" + userName + "][" + password + "]")
      // TODO ログインエラーメッセージの設定
      render("/root/index")
    }
  }
}
