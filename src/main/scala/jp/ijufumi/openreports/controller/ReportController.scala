package jp.ijufumi.openreports.controller

import skinny.Params
import skinny.controller.feature.ThymeleafTemplateEngineFeature

class ReportController extends ApplicationController
  with ThymeleafTemplateEngineFeature {

  def requestParams = Params(params)

  def index = {
    render("/report/index")
  }
}
