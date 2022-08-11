package jp.ijufumi.openreports.api.base

import jp.ijufumi.openreports.utils.Logging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{CorsSupport, Ok, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport

abstract class APIServletBase
    extends ScalatraServlet
    with JacksonJsonSupport
    with Logging
    with CorsSupport {
  override protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  options("/*") {
    Ok(headers = Map("Access-Control-Allow-Origin" -> request.getHeader("Origin")))
  }

  after() {
    val statusCode = response.getStatus
    val servletPath = request.getServletPath
    val pathInfo = request.getPathInfo
    val requestPath = servletPath + pathInfo
    val requestMethod = request.getMethod
    val message = s"${requestMethod} ${requestPath} ${statusCode}"
    statusCode match {
      case n if n < 400 => logger.info(message)
      case n if n < 500 => logger.warn(message)
      case _            => logger.error(message)
    }
  }

  def extractBody[T: Manifest](): T = parse(request.body).extract[T]
}
