package jp.ijufumi.openreports.controller

import skinny.controller.feature.ThymeleafTemplateEngineFeature

class ScheduledReportController extends ApplicationController
  with ThymeleafTemplateEngineFeature {
  def index = {
    render("/scheduled_report/index")
  }
}
