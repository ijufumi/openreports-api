package jp.ijufumi.openreports.service

import java.io.{File, InputStream}
import java.nio.file.{FileSystems, Files}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import jp.ijufumi.openreports.service.support.ConnectionFactory
import org.jxls.common.Context
import org.jxls.jdbc.JdbcHelper
import org.jxls.util.JxlsHelper
import skinny.logging.LoggerProvider

case class ReportingOutputService() extends LoggerProvider {

  def output(templateFile: String,
             param: Map[String, String] = Map.empty): Option[File] = {

    logger.debug("templateFile:%s, param:%s".format(templateFile, param))

    val inFileName = new File(templateFile).getName
    val dotIndex = inFileName.lastIndexOf('.')
    val suffix = if (dotIndex != -1) inFileName.substring(dotIndex) else ""
    val timeStamp =
      DateTimeFormatter.ofPattern("yyyyMMddHHMMss").format(LocalDateTime.now())
    val outputFile = FileSystems.getDefault.getPath(
      OutputFilePath,
      "/tmp/%s_%s%s"
        .format(inFileName.substring(0, dotIndex), timeStamp, suffix)
    )
    // val outputFile = new File("/tmp/%s_%s%s".format(inFileName.substring(0, dotIndex), timeStamp, suffix))

    val outputDirectory = outputFile.getParent
    if (!Files.isDirectory(outputDirectory)) {
      Files.createDirectories(outputDirectory)
    }

    var con: java.sql.Connection = null
    try {
      con = ConnectionFactory.getConnection
      val jdbcHelper = new JdbcHelper(con)
      val context = new Context()
      context.putVar("conn", con)
      context.putVar("jdbc", jdbcHelper)

      val in = getClass.getClassLoader.getResourceAsStream(templateFile)
      val out = Files.newOutputStream(outputFile)
      JxlsHelper
        .getInstance()
        .processTemplate(toInputStream(templateFile), out, context)
    } catch {
      case e: java.io.IOException => {
        logger.error(e.getMessage, e)
        return Option.empty
      }
    } finally {
      if (con != null) {
        con.close()
      }
    }

    Option.apply(outputFile.toFile)
  }

  def toInputStream(templateFile: String): InputStream = {
    val fullPath = FileSystems.getDefault.getPath(TemplatePath, templateFile)
    if (!fullPath.toString.startsWith("/")) {
      getClass.getClassLoader.getResourceAsStream(
        fullPath.toString
      )
    } else {
      Files.newInputStream(fullPath)
    }
  }
}
