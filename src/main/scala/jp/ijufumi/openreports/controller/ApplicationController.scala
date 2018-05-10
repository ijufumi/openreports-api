package jp.ijufumi.openreports.controller

import java.io.{ BufferedInputStream, FileInputStream, InputStream }

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

  def fileDownload(in: String, fileName: String, contentType: String): Unit = {
    var fileStream: InputStream = null
    try {
      fileStream = getClass.getClassLoader.getResourceAsStream(in)
      if (fileStream == null) {
        fileStream = new FileInputStream(new java.io.File(in))
      }
      fileDownload(fileStream, fileName, contentType)
    } finally {
      if (fileStream != null) {
        fileStream.close()
      }
    }
  }

  def fileDownload(in: InputStream, fileName: String, contentType: String): Unit = {
    val inner = withOutputStream { implicit s =>
      response.addHeader("Content-Type", contentType)
      response.setHeader(
        "Content-Disposition", "attachment; filename=\"%s\"".format(fileName)
      );
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

            writeChunk(buffer.slice(0, len))
          }

        } catch {
          case e: java.io.IOException => logger.error(e.getMessage, e)
        }
      }
    }
  }

  def deleteFile(file: String): Unit = {
    val deleteFile = new java.io.File(file)
    if (deleteFile.exists() && deleteFile.isFile) {
      deleteFile.delete()
    }
  }
}
