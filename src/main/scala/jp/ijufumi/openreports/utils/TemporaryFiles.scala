package jp.ijufumi.openreports.utils

import java.nio.file.{Files, Path, StandardOpenOption}

object TemporaryFiles {
  def createDir(): TemporaryDir = {
    new TemporaryDir()
  }

  def createFile(dir: Path, deleteOnClose: Boolean = false, suffix: String = ""): TemporaryFile = {
    new TemporaryFile(dir, deleteOnClose = deleteOnClose, suffix = suffix)
  }
}

class TemporaryDir extends AutoCloseable {
  private val dir = Files.createDirectories(Path.of("/tmp", Strings.generateRandomSting(10)()))

  def path(): Path = {
    dir.getFileName
  }

  override def close(): Unit = {
    Files.deleteIfExists(dir)
  }
}

class TemporaryFile(dir: Path, deleteOnClose: Boolean = false, suffix: String = "")
    extends AutoCloseable {
  private val file =
    Files.createFile(Path.of(dir.toString, s"${Strings.generateRandomSting(10)()}${suffix}"))

  def write(bytes: Array[Byte], append: Boolean = false): Unit = {
    val option = if (append) StandardOpenOption.APPEND else StandardOpenOption.TRUNCATE_EXISTING
    Files.write(file, bytes, option)
  }

  def path(): Path = {
    file
  }

  override def close(): Unit = {
    if (deleteOnClose) {
      Files.deleteIfExists(file)
    }
  }
}
