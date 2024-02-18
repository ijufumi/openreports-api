package jp.ijufumi.openreports.infrastructure.filestores.local.impl

import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.infrastructure.filestores.local.LocalFileRepository

import java.nio.file.{FileSystems, Files, Path}

class LocalFileRepositoryImpl extends LocalFileRepository {
  override def get(workspaceId: String, key: String): Path = {
    FileSystems.getDefault.getPath(Config.TEMPLATE_ROOT_PATH, workspaceId, key)
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {
    val fullPath = FileSystems.getDefault.getPath(Config.TEMPLATE_ROOT_PATH, workspaceId, key)
    Files.createDirectories(fullPath.getParent)
    Files.copy(file, fullPath)
  }

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???
}
