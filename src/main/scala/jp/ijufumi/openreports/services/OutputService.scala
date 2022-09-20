package jp.ijufumi.openreports.services

import scala.reflect.io.File

trait OutputService {
  def output(filePath: String, dataSourceId: String): Option[File]
}
