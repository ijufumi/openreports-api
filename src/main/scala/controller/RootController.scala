package controller

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
    val memberInfo: Option[Any] = skinnySession.getAttribute("memberInfo");
    if (!memberInfo.isDefined) {
      render("/root/index")
    } else {
      redirect("/home")
    }
  }

  def login = {
    if (validateParams.validate) {
      val userName = requestParams.getAs("userName").getOrElse("")
      val password = requestParams.getAs("password").getOrElse("")
      if ("admin".equals(userName) && "admin".equals(password)) {
        //        session += "memberInfo" -> "aaaaa"
        redirect("/home")
      }
      set("userName", requestParams.getAs("userName").getOrElse(""))
      render("/root/index")
    }
  }
}
