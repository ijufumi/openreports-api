package jp.ijufumi.openreports.controller

import java.io.{ BufferedInputStream, InputStream }

import skinny.Params
import scala.util.control.Breaks

class ReportController extends ApplicationController {

  def requestParams = Params(params)

  def download: Unit = {
    val fileStream = getClass.getClassLoader.getResourceAsStream("logback.xml")
    fileDownload(fileStream, "logback.xml", "application/xml")
  }

  def download2 = withOutputStream { implicit s =>
    val fileStream = getClass.getClassLoader.getResourceAsStream("logback.xml")

    val b = new Breaks
    b.breakable {
      try {
        val inputStream = new BufferedInputStream(fileStream)
        var len = 0
        val buffer = new Array[Byte](1024 * 1024)

        while (true) {
          len = inputStream.read(buffer)

          if (len == -1) {
            b.break
          }

          writeChunk(buffer)
        }
      } catch {
        case e: java.io.IOException => logger.error(e.getMessage, e)
      }
    }
  }
}
