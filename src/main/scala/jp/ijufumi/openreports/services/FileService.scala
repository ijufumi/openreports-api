package jp.ijufumi.openreports.services

import java.io.InputStream

trait FileService {
  def url(workspaceId: String, templateId: String): String

  def get(workspaceId: String, templateId: String): InputStream

  def create(workspaceId: String, key: String, file: InputStream): Unit

  def delete(workspaceId: String, templateId: String): Unit
}
