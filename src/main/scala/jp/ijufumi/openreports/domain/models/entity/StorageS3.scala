package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.Dates

case class StorageS3(
    id: String,
    workspaceId: String,
    awsAccessKeyId: String = "",
    awsSecretAccessKey: String = "",
    awsRegion: String = "",
    s3BucketName: String = "",
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)
