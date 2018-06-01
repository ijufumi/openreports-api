package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import skinny.Params
import skinny.controller.feature.ThymeleafTemplateEngineFeature

class ScheduledReportController extends ApplicationController {

  val path = privatePath + "/scheduled_report"

  def requestParams = Params(params)

  def index = {
    render("/scheduled_report/index")
  }
}
