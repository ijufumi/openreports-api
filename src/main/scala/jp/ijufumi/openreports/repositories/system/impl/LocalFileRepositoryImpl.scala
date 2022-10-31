package jp.ijufumi.openreports.repositories.system.impl

import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.repositories.system.LocalFileRepository

import java.io.InputStream
import java.nio.file.{Files, FileSystems}

class LocalFileRepositoryImpl extends LocalFileRepository {
  override def get(key: String): InputStream = {
    val fullPath = FileSystems.getDefault.getPath(Config.OUTPUT_FILE_PATH, key)
    if (!fullPath.toString.startsWith("/")) {
      getClass.getClassLoader.getResourceAsStream(
        fullPath.toString,
      )
    } else {
      Files.newInputStream(fullPath)
    }
  }

  override def create(key: String, file: InputStream): Unit = ???

  override def delete(key: String): Unit = ???

  override def url(key: String): String = ???
}
