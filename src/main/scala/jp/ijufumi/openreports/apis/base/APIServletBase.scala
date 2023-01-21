package jp.ijufumi.openreports.apis.base

import jp.ijufumi.openreports.utils.Logging
import jp.ijufumi.openreports.entities.enums.{RoleTypes, StorageTypes}
import org.json4s.{DefaultFormats, Formats}
import org.json4s.ext.EnumNameSerializer
import org.scalatra.{
  ActionResult,
  BadRequest,
  CorsSupport,
  Forbidden,
  InternalServerError,
  NotFound,
  Ok,
  ScalatraServlet,
  Unauthorized,
}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.forms._
import org.scalatra.i18n.I18nSupport

abstract class APIServletBase
    extends ScalatraServlet
    with JacksonJsonSupport
    with FormSupport
    with I18nSupport
    with Logging
    with CorsSupport {

  override protected implicit lazy val jsonFormats: Formats = {
    DefaultFormats ++ org.json4s.ext.JavaTimeSerializers.all
    DefaultFormats + new EnumNameSerializer(StorageTypes)
    DefaultFormats + new EnumNameSerializer(RoleTypes)
  }

  before() {
    contentType = formats("json")
  }

  options("/*") {
    Ok(headers = Map("Access-Control-Allow-Origin" -> request.getHeader("Origin")))
  }

  def ok(obj: Any): ActionResult = {
    hookResult(Ok(obj))
  }

  // 4xx
  def notFound(obj: Any): ActionResult = {
    hookResult(NotFound(obj))
  }

  def badRequest(obj: Any): ActionResult = {
    hookResult(BadRequest(obj))
  }

  def unauthorized(obj: Any): ActionResult = {
    hookResult(Unauthorized(obj))
  }

  def forbidden(obj: Any): ActionResult = {
    hookResult(Forbidden(obj))
  }

  // 5xx
  def internalServerError(obj: Any): ActionResult = {
    hookResult(InternalServerError(obj))
  }

  def params(key: String, default: String): String = {
    params(request).getOrElse(key, default)
  }

  private def hookResult(actionResult: ActionResult): ActionResult = {
    val servletPath = request.getRequestURI
    val queryString = request.getQueryString
    val requestMethod = request.getMethod
    val requestPath =
      if (queryString == null || queryString.isEmpty) servletPath
      else s"${servletPath}?${queryString}"
    val message = s"${requestMethod} ${requestPath} ${actionResult.status}"
    actionResult.status match {
      case n if 400 <= n && n < 500 => logger.warn(message)
      case n if 500 <= n            => logger.error(message)
      case _                        => logger.info(message)
    }
    actionResult
  }

  error {
    case t => {
      logger.error(t.getMessage, t)
      internalServerError(t)
    }
  }

  def extractBody[T: Manifest](): T = parse(request.body).extract[T]
}
