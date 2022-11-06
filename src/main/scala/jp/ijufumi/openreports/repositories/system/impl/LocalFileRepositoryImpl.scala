package jp.ijufumi.openreports.repositories.system.impl

import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.repositories.system.LocalFileRepository

import java.io.{File, InputStream}
import java.nio.file.{Files, FileSystems}

class LocalFileRepositoryImpl extends LocalFileRepository {
  override def get(workspaceId: String, key: String): InputStream = {
    val fullPath = FileSystems.getDefault.getPath(Config.TEMPLATE_PATH, workspaceId, key)
    if (!fullPath.toString.startsWith("/")) {
      getClass.getClassLoader.getResourceAsStream(
        fullPath.toString,
      )
    } else {
      Files.newInputStream(fullPath)
    }
  }

  override def create(workspaceId: String, key: String, file: File): Unit = ???

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???
}
