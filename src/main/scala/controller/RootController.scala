package controller

import skinny._
import skinny.controller.feature.ThymeleafTemplateEngineFeature

class RootController extends ApplicationController
with ThymeleafTemplateEngineFeature {

  def index = render("/root/index")

}
