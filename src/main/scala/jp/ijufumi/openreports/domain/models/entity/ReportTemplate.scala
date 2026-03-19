package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.utils.Dates

case class ReportTemplate(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    storageType: StorageTypes.StorageType,
    fileSize: Long,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)
