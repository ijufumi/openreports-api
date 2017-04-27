package jp.ijufumi.openreports.controller

import skinny._
import skinny.controller.feature.ThymeleafTemplateEngineFeature
import skinny.validator.{ required, _ }

class HomeController extends ApplicationController
    with ThymeleafTemplateEngineFeature {

  def index = {
    val memberInfo: Option[Any] = skinnySession.getAttribute("memberInfo")
    if (memberInfo.isDefined) {
      render("/home/index")
    } else {
      redirect("/")
    }
  }

  def logout = {
    skinnySession.removeAttribute("memberInfo");
    redirect("/")
  }
}
