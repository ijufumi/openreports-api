package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import skinny.controller.feature.ThymeleafTemplateEngineFeature

class HomeController extends ApplicationController
  with ThymeleafTemplateEngineFeature {
  val path = privatePath + "/home"
  val viewPath = privatePath + "/home"

  def index = {
    //logger.info("index called at " + getClass.getSimpleName)
    val memberInfo: Option[Any] = skinnySession.getAttribute("memberInfo")
    if (memberInfo.isDefined) {
      //set("loggedIn", memberInfo.isDefined.booleanValue())
      render(viewPath + "/index")
    } else {
      redirect(publicPath)
    }
  }

  def logout = {
    skinnySession.removeAttribute("memberInfo");
    redirect(publicPath)
  }
}
