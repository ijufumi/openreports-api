package controller

import skinny._
import skinny.controller.feature.ThymeleafTemplateEngineFeature
import skinny.validator._
import skinny.validator.required

class RootController extends ApplicationController
    with ThymeleafTemplateEngineFeature {

  def requestParams = Params(params)

  def validateParams = validation(
    requestParams,
    paramKey("userName") is required,
    paramKey("password") is required
  )

  def index = render("/root/index")

  def login = {
    if (validateParams.validate) {
      render("/home/index")
    } else {
      render("/root/index")
    }
  }
}
