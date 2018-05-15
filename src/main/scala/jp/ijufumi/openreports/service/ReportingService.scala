package jp.ijufumi.openreports.service

import java.io.{ File, FileOutputStream }
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.jxls.common.Context
import org.jxls.jdbc.JdbcHelper
import org.jxls.util.JxlsHelper
import skinny.logging.LoggerProvider

class ReportingService(templateFile: String) extends LoggerProvider {
  def output(): String = {
    val inFileName = new File(templateFile).getName
    val dotIndex = inFileName.lastIndexOf('.')
    val suffix = if (dotIndex != -1) inFileName.substring(dotIndex) else ""
    val timeStamp = DateTimeFormatter.ofPattern("yyyyMMddHHMMss").format(LocalDateTime.now())
    val outputFile = "/tmp/%s_%s%s".format(inFileName.substring(0, dotIndex), timeStamp, suffix)

    val outputDirectory = new File(outputFile).getParentFile
    if (!outputDirectory.exists()) {
      outputDirectory.mkdirs()
    }

    var con: java.sql.Connection = null
    try {
      con = ConnectionFactory.getConnection("jdbc:postgresql://localhost:5432/openreports", "postgres", "password")
      val jdbcHelper = new JdbcHelper(con)
      val context = new Context()
      context.putVar("conn", con)
      context.putVar("jdbc", jdbcHelper)

      var in = getClass.getClassLoader.getResourceAsStream(templateFile)
      var out = new FileOutputStream(outputFile)
      JxlsHelper.getInstance().processTemplate(in, out, context)
    } catch {
      case e: java.io.IOException => {
        logger.error(e.getMessage, e)
        return ""
      }
    } finally {
      if (con != null) {
        con.close()
      }
    }

    outputFile
  }
}