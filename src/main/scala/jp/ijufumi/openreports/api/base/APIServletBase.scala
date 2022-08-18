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
    val servletPath = request.getServletPath
    val pathInfo = request.getPathInfo
    val requestPath = servletPath + pathInfo
    val requestMethod = request.getMethod
    val message = s"${requestMethod} ${requestPath} ${status}"
    status match {
      case n if 400 <= n && n < 500 => logger.warn(message)
      case n if 500 <= n            => logger.error(message)
      case _                        => logger.info(message)
    }
  }

  error {
    case t => {
      logger.error(t.getMessage, t)
      throw t
    }
  }

  def extractBody[T: Manifest](): T = parse(request.body).extract[T]
}
