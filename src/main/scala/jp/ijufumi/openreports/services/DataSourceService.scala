package jp.ijufumi.openreports.services

import java.sql.Connection

trait DataSourceService {
  def connection(dataSourceId: String): Option[Connection]
}
