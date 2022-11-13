package jp.ijufumi.openreports.repositories.system.impl

import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.repositories.system.LocalFileRepository

import java.nio.file.{Files, FileSystems, Path}

class LocalFileRepositoryImpl extends LocalFileRepository {
  override def get(workspaceId: String, key: String): Path = {
    FileSystems.getDefault.getPath(Config.TEMPLATE_ROOT_PATH, workspaceId, key)
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {
    val fullPath = FileSystems.getDefault.getPath(Config.TEMPLATE_ROOT_PATH, workspaceId, key)
    Files.copy(file, fullPath)
  }

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???
}
