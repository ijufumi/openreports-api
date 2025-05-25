package jp.ijufumi.openreports.infrastructure.filestores.local.impl

import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.infrastructure.filestores.local.LocalSeedFileRepository

import java.nio.file.{Files, FileSystems, Path}

class LocalSeedFileRepositoryImpl extends LocalSeedFileRepository {
  override def get(workspaceId: String, key: String): Path = {
    FileSystems.getDefault.getPath(Config.SEED_TEMPLATE_ROOT_PATH, key)
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {}

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???
}
