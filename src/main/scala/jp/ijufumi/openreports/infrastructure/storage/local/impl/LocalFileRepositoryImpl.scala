package jp.ijufumi.openreports.infrastructure.storage.local.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.infrastructure.storage.local.LocalFileRepository

import java.nio.file.{Files, FileSystems, Path}

class LocalFileRepositoryImpl @Inject() (implicit
    templateRootPath: String = Config.TEMPLATE_ROOT_PATH,
) extends LocalFileRepository {
  override def get(workspaceId: String, key: String): Path = {
    resolveSafePath(workspaceId, key)
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {
    val fullPath = resolveSafePath(workspaceId, key)
    Files.createDirectories(fullPath.getParent)
    Files.copy(file, fullPath)
  }

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???

  private def resolveSafePath(workspaceId: String, key: String): Path = {
    val root = FileSystems.getDefault.getPath(templateRootPath).toAbsolutePath.normalize()
    val resolved = root.resolve(workspaceId).resolve(key).normalize()
    if (!resolved.startsWith(root)) {
      throw new IllegalArgumentException(
        s"path traversal detected: workspaceId=$workspaceId, key=$key",
      )
    }
    resolved
  }
}
