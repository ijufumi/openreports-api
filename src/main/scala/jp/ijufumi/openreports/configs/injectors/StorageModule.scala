package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.infrastructure.storage.local.impl.{
  LocalFileRepositoryImpl,
  LocalSeedFileRepositoryImpl,
}
import jp.ijufumi.openreports.infrastructure.storage.local.{
  LocalFileRepository,
  LocalSeedFileRepository,
}
import jp.ijufumi.openreports.infrastructure.storage.s3.AwsS3Repository
import jp.ijufumi.openreports.infrastructure.storage.s3.impl.{
  AwsS3RepositoryImpl,
  DefaultS3ClientFactory,
  S3ClientFactory,
}

class StorageModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindClass(classOf[S3ClientFactory], classOf[DefaultS3ClientFactory])

    // file store
    bindClass(classOf[LocalFileRepository], classOf[LocalFileRepositoryImpl])
    bindClass(classOf[LocalSeedFileRepository], classOf[LocalSeedFileRepositoryImpl])
    bindClass(classOf[AwsS3Repository], classOf[AwsS3RepositoryImpl])

  }
}
