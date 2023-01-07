package jp.ijufumi.openreports.services

import java.sql.Connection
import jp.ijufumi.openreports.vo.response.DataSource

trait DataSourceService {
  def connection(workspaceId: String, dataSourceId: String): Connection

  def getDataSources(workspaceId: String): Seq[DataSource]
}
