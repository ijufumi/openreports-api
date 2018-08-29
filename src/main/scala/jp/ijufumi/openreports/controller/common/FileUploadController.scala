package jp.ijufumi.openreports.controller.common

import java.io.{BufferedInputStream, FileInputStream, InputStream}
import java.time.{LocalDateTime, ZoneOffset}

import jp.ijufumi.openreports.controller.PublicPath
import skinny.{SkinnyEnv, SkinnyServlet}
import skinny.controller.feature.{FileUploadFeature, ThymeleafTemplateEngineFeature}
import skinny.filter.{ErrorPageFilter, SkinnySessionFilter}

import scala.util.control.Breaks

trait FileUploadController
    extends SkinnyServlet
    with SkinnySessionFilter
    with ErrorPageFilter
    with I18nFeature
    with ThymeleafTemplateEngineFeature
    with FileUploadFeature {

  val activeMenu = ""

  beforeAction() {
    val memberInfo = skinnySession.getAs("memberInfo")
    // logger.info("before called at " + getClass.getSimpleName + ":" + memberInfo.isDefined)
    set("loggedIn", memberInfo.isDefined)
    if (requiredMemberInfo && memberInfo.isEmpty) {
      redirect(PublicPath)
    }

    set("activeMenu", activeMenu)
    set("timeStamp", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
  }
  val requiredMemberInfo = false

  def fileDownload(in: String,
                   fileName: String,
                   contentType: String,
                   streamClose: Boolean = true): Unit = {
    var fileStream: InputStream = null
    try {
      fileStream = getClass.getClassLoader.getResourceAsStream(in)
      if (fileStream == null) {
        fileStream = new FileInputStream(new java.io.File(in))
      }
      fileDownload(fileStream, fileName, contentType)
    } finally {
      if (streamClose && fileStream != null) {
        fileStream.close()
      }
    }
  }

  def fileDownload(in: InputStream, fileName: String, contentType: String): Unit = {
    val inner = withOutputStream { implicit s =>
      response.addHeader("Content-Type", contentType)
      response.setHeader(
        "Content-Disposition",
        "attachment; filename=\"%s\"".format(fileName)
      )
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
    deleteFile(new java.io.File(file))
  }

  def deleteFile(deleteFile: java.io.File): Unit = {
    if (deleteFile.exists() && deleteFile.isFile) {
      deleteFile.delete()
    }
  }
}
