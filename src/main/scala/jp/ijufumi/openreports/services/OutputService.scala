package jp.ijufumi.openreports.services

import java.io.File


trait OutputService {
  def output(filePath: String, dataSourceId: String): Option[File]
}
