package jp.ijufumi.openreports.api

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

abstract class APIServletBase extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit lazy val jsonFormats: Formats = DefaultFormats
}
