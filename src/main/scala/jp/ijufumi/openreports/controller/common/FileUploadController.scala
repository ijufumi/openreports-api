package jp.ijufumi.openreports.controller.common

import skinny.SkinnyServlet
import skinny.controller.feature.{FileUploadFeature, ThymeleafTemplateEngineFeature}
import skinny.filter.{ErrorPageFilter, SkinnySessionFilter}

trait FileUploadController extends SkinnyServlet
  with SkinnySessionFilter
  with ErrorPageFilter
  with I18nFeature
  with ThymeleafTemplateEngineFeature
  with ControllerCommonFeature
  with FileUploadFeature {

}
