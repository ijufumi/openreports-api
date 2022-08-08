package jp.ijufumi.openreports.api.base

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.{Logger, LoggerFactory}

abstract class APIServletBase extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected val logger: Logger = LoggerFactory.getLogger(getClass);

  before() {
    contentType = formats("json")
  }

  after() {
    val statusCode = response.getStatus
    val servletPath = request.getServletPath
    val pathInfo = request.getPathInfo
    val requestPath = servletPath + pathInfo
    statusCode match {
      case n if n < 400 => logger.info(s"${requestPath} ${statusCode}")
      case n if n < 500 => logger.warn(s"${requestPath} ${statusCode}")
      case _            => logger.error(s"${requestPath} ${statusCode}")
    }
  }

  def extractBody[T: Manifest](): T = parse(request.body).extract[T]
}
