package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.Dates

case class Report(
    id: String,
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    reportTemplate: Option[ReportTemplate] = None,
)
