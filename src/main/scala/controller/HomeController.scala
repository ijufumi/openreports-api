package controller

import skinny._
import skinny.controller.feature.ThymeleafTemplateEngineFeature
import skinny.validator.{ required, _ }

class HomeController extends ApplicationController
    with ThymeleafTemplateEngineFeature {

  def index = render("/home/index")

  def logout = {
    // TODO: セッションクリア
    redirect("/")
  }
}
