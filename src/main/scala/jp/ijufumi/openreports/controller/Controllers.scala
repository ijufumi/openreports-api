package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.Controllers.scheduledReport.{
  get,
  index,
  path
}

import skinny._
import skinny.controller.AssetsController

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    top$.mount(ctx)
    home.mount(ctx)
    report.mount(ctx)
    scheduledReport.mount(ctx)

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
    val prepareUrl = get(path + "/prepare/:id")(prepareReport).as('prepare)
    val paramsUrl = post(path + "/params")(setParams).as('setParams)
    val printOutReportUrl =
      get(path + "/printOut/:id")(printOutReport).as('printOutReport)
    val downloadUrl = get(path + "/download")(download).as('download)
  }

  object scheduledReport extends ScheduledReportController with Routes {
    val indexUrl = get(path + "/")(index).as('index)
  }

}
