package jp.ijufumi.openreports.repositories.system

import java.io.{File, InputStream}

trait FileStorageRepository {
  def url(workspaceId: String, key: String): String

  def get(workspaceId: String, key: String): InputStream

  def create(workspaceId: String, key: String, file: File): Unit

  def delete(workspaceId: String, key: String): Unit
}
