package jp.ijufumi.openreports.controller.common

import jp.ijufumi.openreports.vo.ApiResponse
import skinny._
import skinny.controller.feature.JSONFeature

/**
  * The base jp.ijufumi.openreports.controller for API endpoints.
  */
trait ApiController extends SkinnyApiController with JSONFeature with I18nFeature {

  /*
   * Handles when unexpected exceptions are thrown from controllers.
   */
  addErrorFilter {
    case e: Throwable =>
      // For example, logs a exception and responds with status 500.
      logger.error(e.getMessage, e)
      haltWithBody(500)
  }

  def okResponse(entity: Any): String = {
    responseAsJSON(ApiResponse(message = entity))
  }

  def ngResponse(message: Option[String]): String = {
    responseAsJSON(ApiResponse(status = false, message = message.getOrElse("error")))
  }
}
