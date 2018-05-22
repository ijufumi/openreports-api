package jp.ijufumi.openreports.controller.common

import jp.ijufumi.openreports.controller.{privatePath, publicPath}
//import jp.ijufumi.openreports.controller.HomeController
import jp.ijufumi.openreports.controller.settings.ReportSettingsController
import jp.ijufumi.openreports.controller.{HomeController, ReportController, RootController, ScheduledReportController}
import skinny._
import skinny.controller.AssetsController

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    root.mount(ctx)
    home.mount(ctx)
    report.mount(ctx)
    scheduledReport.mount(ctx)
    reportSettings.mount(ctx)

    AssetsController.mount(ctx)
  }

  object root extends RootController with Routes {
    val toTopUrl = get("/?")(toTop).as('toTop)
    val indexUrl = get(publicPath + "/?")(index).as('index)
    val indexUrl2 = get(publicPath + "/login")(index).as('index)
    val loginUrl = post(publicPath + "/login")(login).as('login)
  }

  object home extends HomeController with Routes {
    val indexUrl = get(path + "/?")(index).as('index)
    val logoutUrl = get(path + "/logout/?")(logout).as('logout)
  }

  object report extends ReportController with Routes {
    val downloadUrl = get(privatePath + "/report/download")(download).as('download)
    val outputUrl = get(privatePath + "/report/output")(outputReport).as('output)
  }

  object scheduledReport extends ScheduledReportController with Routes {
    val indexUrl = get(privatePath + "/scheduled_report/")(index).as('index)
  }

  object reportSettings extends ReportSettingsController with Routes {
    val indexUrl = get(privatePath + "/report/fileUpload/form")(index).as('index)
  }
}
