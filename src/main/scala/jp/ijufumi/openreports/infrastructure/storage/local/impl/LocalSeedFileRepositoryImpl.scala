package jp.ijufumi.openreports.infrastructure.storage.local.impl

import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.infrastructure.storage.local.LocalSeedFileRepository

import java.nio.file.{FileSystems, Path}

class LocalSeedFileRepositoryImpl extends LocalSeedFileRepository {
  override def get(workspaceId: String, key: String): Path = {
    val root = FileSystems.getDefault
      .getPath(Config.SEED_TEMPLATE_ROOT_PATH)
      .toAbsolutePath
      .normalize()
    val resolved = root.resolve(key).normalize()
    if (!resolved.startsWith(root)) {
      throw new IllegalArgumentException(s"path traversal detected: key=$key")
    }
    resolved
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {}

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???
}
