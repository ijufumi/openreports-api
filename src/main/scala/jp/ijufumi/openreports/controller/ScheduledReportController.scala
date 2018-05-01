package jp.ijufumi.openreports.controller

import skinny.Params
import skinny.controller.feature.ThymeleafTemplateEngineFeature

class ScheduledReportController extends ApplicationController
    with ThymeleafTemplateEngineFeature {

  def requestParams = Params(params)

  def index = {
    render("/scheduled_report/index")
  }
}
