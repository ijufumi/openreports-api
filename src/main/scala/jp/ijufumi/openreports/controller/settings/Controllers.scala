package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.settings.Controllers.memberSettings.{get, index, path}
import skinny._

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    reportSettings.mount(ctx)
    memberSettings.mount(ctx)
    groupSettings.mount(ctx)
  }

  object reportSettings extends ReportSettingsController with Routes {
    val indexUrl = get(path + "/?")(index).as('index)
    val uploadFormUrl =
      get(path + "/fileUpload/form")(uploadForm).as('uploadForm)
  }

  object memberSettings extends MemberSettingsController with Routes {
    val indexUrl = get(path + "/?")(index).as('index)
    val showRegisterUrl =
      get(path + "/register")(showRegister).as('showRegister)
    val doRegisterUrl = post(path + "/register")(doRegister).as('doRegister)
    val registerCompletedUrl =
      get(path + "/registerCompleted")(registerCompleted).as('registerCompleted)
    val showRpdateUrl = get(path + "/update/:id")(showUpdate).as('showUpdate)
    val doUpdateUrl = post(path + "/update/:id")(doUpdate).as('doUpdate)
    val updateCompletedUrl =
      get(path + "/updateCompleted")(updateCompleted).as('updateCompleted)
  }

  object groupSettings extends GroupSettingsController with Routes {
    val indexUrl = get(path + "/?")(index).as('index)
    val showRegisterUrl =
      get(path + "/register")(showRegister).as('showRegister)
    val doRegisterUrl = post(path + "/register")(doRegister).as('doRegister)
    val registerCompletedUrl =
      get(path + "/registerCompleted")(registerCompleted).as('registerCompleted)
    val showRpdateUrl = get(path + "/update/:id")(showUpdate).as('showUpdate)
    val doUpdateUrl = post(path + "/update/:id")(doUpdate).as('doUpdate)
    val updateCompletedUrl =
      get(path + "/updateCompleted")(updateCompleted).as('updateCompleted)
  }

}
