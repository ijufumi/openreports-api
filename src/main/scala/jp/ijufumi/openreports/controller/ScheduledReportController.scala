package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import skinny.Params

class ScheduledReportController extends ApplicationController {

  val path = PrivatePath + "/scheduled_report"

  def requestParams = Params(params)

  def index = {
    render("/scheduled_report/index")
  }
}
