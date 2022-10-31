package jp.ijufumi.openreports.repositories.system

import java.io.InputStream

trait FileStorageRepository {
  def url(key: String): String

  def get(key: String): InputStream

  def create(key: String, file: InputStream): Unit

  def delete(key: String): Unit
}
