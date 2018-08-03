package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController

class HomeController extends ApplicationController {
  override val activeMenu = "home"
  override val requiredMemberInfo = true
  val path = privatePath + "/home"
  val viewPath = privatePath + "/home"

  def index = {
    render(viewPath + "/index")
  }

  def logout = {
    skinnySession.removeAttribute("memberInfo");
    redirect(publicPath)
  }
}
