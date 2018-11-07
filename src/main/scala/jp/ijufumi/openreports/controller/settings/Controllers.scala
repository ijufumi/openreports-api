package jp.ijufumi.openreports.controller.settings

import skinny._

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    reportSettings.mount(ctx)
    reportGroupSettings.mount(ctx)
    reportTemplateSettings.mount(ctx)
    reportParamSettings.mount(ctx)

    memberSettings.mount(ctx)
    groupSettings.mount(ctx)
  }

  object reportSettings extends ReportSettingsController with Routes {
    val indexUrl = get(path)(index).as('index)

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

  object reportGroupSettings extends ReportGroupSettingsController with Routes {
    val indexUrl = get(path)(index).as('index)

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

  object reportTemplateSettings extends ReportTemplateSettingsController with Routes {
    val indexUrl = get(path)(index).as('index)

    val showHistoryUrl = get(path + "/history/:id")(showHistory).as('showHistory)

    val showUploadUrl = get(path + "/upload")(showUpload).as('showUpload)

    val uploaderUrl = post(path + "/upload")(upload).as('upload)
  }

  object reportParamSettings extends ReportParamSettingsController with Routes {
    val indexUrl = get(path)(index).as('index)

    val showRegisterUrl =
      get(path + "/register")(showRegister).as('showRegister)

    val doRegisterUrl = post(path + "/register")(doRegister).as('doRegister)

    val registerCompletedUrl =
      get(path + "/registerCompleted")(registerCompleted).as('registerCompleted)

    val showUpdateUrl =
      get(path + "/update/:id")(showUpdate).as('showUpdate)

    val doUpdateUrl = post(path + "/update/:id")(doUpdate).as('doUpdate)

    val updateCompletedUrl =
      get(path + "/updateCompleted")(updateCompleted).as('updateCompleted)

  }

  object memberSettings extends MemberSettingsController with Routes {
    val indexUrl = get(path)(index).as('index)

    val showRegisterUrl =
      get(path + "/register")(showRegister).as('showRegister)

    val doRegisterUrl = post(path + "/register")(doRegister).as('doRegister)

    val registerCompletedUrl =
      get(path + "/registerCompleted")(registerCompleted).as('registerCompleted)

    val showUpdateUrl = get(path + "/update/:id")(showUpdate).as('showUpdate)

    val doUpdateUrl = post(path + "/update/:id")(doUpdate).as('doUpdate)

    val updateCompletedUrl =
      get(path + "/updateCompleted")(updateCompleted).as('updateCompleted)
  }

  object groupSettings extends GroupSettingsController with Routes {
    val indexUrl = get(path)(index).as('index)

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
