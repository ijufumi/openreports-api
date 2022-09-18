package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.repositories.db.DataSourceRepository
import jp.ijufumi.openreports.services.DataSourceService

import java.sql.Connection

class DataSourceServiceImpl @Inject() (dataSourceRepository: DataSourceRepository)
    extends DataSourceService {
  def connection(dataSourceId: String): Connection = {
    val dataSource = dataSourceRepository.getById(dataSourceId)
    if (dataSource.isEmpty) {
      throw new NotFoundException()
    }

  }
}
