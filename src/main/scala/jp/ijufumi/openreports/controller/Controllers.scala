package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.settings.ReportSettingsController
import skinny._
import skinny.controller.AssetsController

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    top$.mount(ctx)
    home.mount(ctx)
    report.mount(ctx)
    scheduledReport.mount(ctx)
    reportSettings.mount(ctx)

    AssetsController.mount(ctx)
  }

  object top$ extends TopController with Routes {
    val toTopUrl = get("/?")(toTop).as('toTop)
    val indexUrl = get(path + "/?")(index).as('index)
    val indexUrl2 = get(path + "/login")(index).as('index)
    val signUpUrl = get(path + "/signup")(signUp).as('signUp)
    val loginUrl = post(path + "/login")(login).as('login)
  }

  object home extends HomeController with Routes {
    val indexUrl = get(path + "/?")(index).as('index)
    val logoutUrl = get(path + "/logout/?")(logout).as('logout)
  }

  object report extends ReportController with Routes {
    val indexUrl = get(path + "/?")(groupList).as('index)
    val reportListUrl = get(path + "/:id")(reportList).as('reportList)
    val outputUrl = get(path + "/output/:id")(outputReport).as('output)
    val downloadUrl = get(path + "/download")(download).as('download)
  }

  object scheduledReport extends ScheduledReportController with Routes {
    val indexUrl = get(path + "/")(index).as('index)
  }

  object reportSettings extends ReportSettingsController with Routes {
    val uploadFormUrl = get(path + "/fileUpload/form")(uploadForm).as('uploadForm)
  }

}
