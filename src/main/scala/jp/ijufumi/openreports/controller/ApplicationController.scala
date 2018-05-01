package jp.ijufumi.openreports.controller

import java.io.{ BufferedInputStream, InputStream }

import skinny._
import skinny.filter._
import scala.util.control.Breaks

/**
 * The base jp.ijufumi.openreports.controller for this Skinny application.
 *
 * see also "http://skinny-framework.org/documentation/jp.ijufumi.openreports.controller-and-routes.html"
 */
trait ApplicationController extends SkinnyController
    // with TxPerRequestFilter
    with SkinnySessionFilter
    with ErrorPageFilter {

  def i18n: I18n = new I18n()

  // override def defaultLocale = Some(new java.util.Locale("ja"))

  def fileDownload(in: InputStream, fileName: String, contentType: String): Unit = {
    val b = new Breaks
    b.breakable {
      try {
        val inputStream = new BufferedInputStream(in)
        var len = 0
        val buffer = new Array[Byte](1024 * 1024)

        while (true) {
          len = inputStream.read(buffer)

          if (len == -1) {
            b.break
          }

          response.getOutputStream.write(buffer, 0, len)
        }

      } catch {
        case e: java.io.IOException => logger.error(e.getMessage, e)
      }
    }
  }
}
