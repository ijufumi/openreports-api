package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  ReportGroup => ReportGroupEntity,
}
import jp.ijufumi.openreports.presentation.models.requests.UpdateReportGroup
import jp.ijufumi.openreports.utils.Dates

case class ReportGroup(
    id: String,
    name: String,
    createdAt: Long,
    updatedAt: Long,
)
