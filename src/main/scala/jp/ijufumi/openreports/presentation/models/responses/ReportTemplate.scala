package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes

case class ReportTemplate(
    id: String,
    name: String,
    filePath: String,
    storageType: StorageTypes.StorageType,
    fileSize: Long,
    createdAt: Long,
    updatedAt: Long,
)
