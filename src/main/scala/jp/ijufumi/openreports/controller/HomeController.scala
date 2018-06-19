package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController

class HomeController extends ApplicationController {
  val path = privatePath + "/home"
  val viewPath = privatePath + "/home"

  override val activeMenu = "home"
  override val requiredMemberInfo = true

  def index = {
    render(viewPath + "/index")
  }

  def logout = {
    skinnySession.removeAttribute("memberInfo");
    redirect(publicPath)
  }
}
