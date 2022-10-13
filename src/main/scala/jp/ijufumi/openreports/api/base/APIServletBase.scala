package jp.ijufumi.openreports.api.base

import jp.ijufumi.openreports.utils.Logging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{ActionResult, CorsSupport, Ok, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport

abstract class APIServletBase
    extends ScalatraServlet
    with JacksonJsonSupport
    with Logging
    with CorsSupport {
  override protected implicit lazy val jsonFormats: Formats =
    DefaultFormats ++ org.json4s.ext.JavaTimeSerializers.all

  before() {
    contentType = formats("json")
  }

  options("/*") {
    Ok(headers = Map("Access-Control-Allow-Origin" -> request.getHeader("Origin")))
  }

  def hookResult(actionResult: ActionResult): ActionResult = {
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
      throw t
    }
  }

  def extractBody[T: Manifest](): T = parse(request.body).extract[T]
}
