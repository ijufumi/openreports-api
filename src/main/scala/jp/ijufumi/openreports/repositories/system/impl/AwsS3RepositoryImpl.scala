package jp.ijufumi.openreports.repositories.system.impl

import jp.ijufumi.openreports.repositories.system.AwsS3Repository

import java.io.InputStream

class AwsS3RepositoryImpl extends AwsS3Repository {
  override def get(key: String): InputStream = ???

  override def create(key: String, file: InputStream): Unit = ???

  override def delete(key: String): Unit = ???

  override def url(key: String): String = ???
}
