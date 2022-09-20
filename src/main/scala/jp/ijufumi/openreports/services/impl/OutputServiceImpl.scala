package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.services.{DataSourceService, OutputService}
import jp.ijufumi.openreports.utils.Logging

import scala.reflect.io.File
import scala.util.Using
import org.jxls.common.Context
import org.jxls.jdbc.JdbcHelper
import org.jxls.util.JxlsHelper

class OutputServiceImpl @Inject() (dataSourceService: DataSourceService)
    extends OutputService
    with Logging {
  override def output(filePath: String, dataSourceId: String): Option[File] = {
    Using(dataSourceService.connection(dataSourceId)) { conn =>
      val jdbcHelper = new JdbcHelper(conn)
      val context = new Context()
      context.putVar("conn", conn)
      context.putVar("jdbc", jdbcHelper)
    }

    None
  }
}
