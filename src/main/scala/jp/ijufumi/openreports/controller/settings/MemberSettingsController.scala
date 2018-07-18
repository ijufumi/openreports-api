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
}
