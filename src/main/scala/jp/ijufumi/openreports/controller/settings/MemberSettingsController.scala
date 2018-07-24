package jp.ijufumi.openreports.controller.settings
import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.SettingMemberService

class MemberSettingsController extends ApplicationController {
  val path = rootPath + "/member"
  val viewPath = rootPath + "/member"

  override val activeMenu = "settings/member"
  override val requiredMemberInfo = true

  def index = {
    val members = new SettingMemberService().getMembers()
    set("members", members)
    render(viewPath + "/index")
  }

  def form = {
    render(viewPath + "/form")
  }

  def register = {
    redirect(path + "/registerCompleted")
  }

  def registerCompleted = {
    render(viewPath + "/register-complete")
  }

  def edit = {
    render(viewPath + "/edit")
  }

  def update = {
    redirect(path + "/updateCompleted")
  }

  def updateCompleted = {
    render(viewPath + "/update-complete")
  }
}
