package jp.ijufumi.openreports.service

import java.io.FileOutputStream

import org.jxls.common.Context
import org.jxls.jdbc.JdbcHelper
import org.jxls.util.JxlsHelper
import skinny.logging.LoggerProvider

class ReportingService(templateFile: String) extends LoggerProvider {
  def output(): String = {
    val outputFile = "/temp/%d_%s".format(java.lang.System.currentTimeMillis(), templateFile)

    var con: java.sql.Connection = null
    try {
      con = ConnectionFactory.getConnection("jdbc:postgresql://localhost:5432/openreports", "postgres", "password")
      val jdbcHelper = new JdbcHelper(con)
      val context = new Context()
      context.putVar("conn", con)
      context.putVar("jdbc", jdbcHelper)

      var in = getClass.getResourceAsStream(templateFile)
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
