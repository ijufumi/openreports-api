package jp.ijufumi.openreports.controller

import skinny._
import skinny.micro.routing.Route

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    login.mount(ctx)
    home.mount(ctx)
    report.mount(ctx)
    scheduledReport.mount(ctx)
  }

  object login extends LoginController with Routes {
    val loginUrl: Route = post(path + "/login")(login).as("login")
  }

  object home extends HomeController with Routes {
    val logoutUrl: Route = get(path + "/logout")(logout).as("logout")
  }

  object report extends ReportController with Routes {
    val indexUrl: Route = get(path)(groupList).as("index")
    val reportListUrl: Route = get(path + "/:id")(reportList).as("reportList")
    val prepareUrl: Route = get(path + "/prepare/:id")(prepareReport).as("prepare")
    val paramsUrl: Route = post(path + "/params")(setParams).as("setParams")
    val downloadUrl: Route = get(path + "/download")(download).as("download")
  }

  object scheduledReport extends ScheduledReportController with Routes {
    val indexUrl: Route = get(path)(index).as("index")
  }
}
