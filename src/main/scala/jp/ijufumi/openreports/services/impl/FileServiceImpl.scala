package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.services.FileService

import java.io.InputStream

class FileServiceImpl extends FileService {
  override def url(workspaceId: String, templateId: String): String = ???

  override def get(workspaceId: String, templateId: String): InputStream = ???

  override def create(workspaceId: String, key: String, file: InputStream): Unit = ???

  override def delete(workspaceId: String, templateId: String): Unit = ???
}
