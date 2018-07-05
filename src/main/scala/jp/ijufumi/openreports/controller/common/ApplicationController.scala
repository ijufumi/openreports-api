package jp.ijufumi.openreports.controller.common

import java.io.{BufferedInputStream, FileInputStream, InputStream}

import jp.ijufumi.openreports.controller.publicPath
import skinny._
import skinny.controller.feature.ThymeleafTemplateEngineFeature
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
  with ErrorPageFilter
  with I18nFeature
  with ThymeleafTemplateEngineFeature {

  val activeMenu = ""
  val requiredMemberInfo = false

  beforeAction() {
    val memberInfo = skinnySession.getAs("memberInfo")
    // logger.info("before called at " + getClass.getSimpleName + ":" + memberInfo.isDefined)
    set("loggedIn", memberInfo.isDefined)
    if (requiredMemberInfo && memberInfo.isEmpty) {
      redirect(publicPath)
    }

    set("activeMenu", activeMenu)
  }

  // override def defaultLocale = Some(new java.util.Locale("ja"))
  override lazy val thymeleafResolverPrefix: String = {
    if (SkinnyEnv.isTest()) "views/"
    else "/views/"
  }

  def fileDownload(in: String, fileName: String, contentType: String, streamClose: Boolean = true): Unit = {
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
        "Content-Disposition", "attachment; filename=\"%s\"".format(fileName)
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
