package jp.ijufumi.openreports.controller

import skinny.controller.feature.ThymeleafTemplateEngineFeature

class ReportController extends ApplicationController
  with ThymeleafTemplateEngineFeature {
  def index = {
    render("/report/index")
  }
}
