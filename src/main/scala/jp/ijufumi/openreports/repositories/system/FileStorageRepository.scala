package jp.ijufumi.openreports.repositories.system

import java.nio.file.Path

trait FileStorageRepository {
  def url(workspaceId: String, key: String): String

  def get(workspaceId: String, key: String): Path

  def create(workspaceId: String, key: String, file: Path): Unit

  def delete(workspaceId: String, key: String): Unit
}
