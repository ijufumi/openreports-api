package jp.ijufumi.openreports.presentation.response

import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  ReportGroup => ReportGroupEntity,
}
import jp.ijufumi.openreports.presentation.request.UpdateReportGroup
import jp.ijufumi.openreports.utils.Dates

case class ReportGroup(
    id: String,
    name: String,
    createdAt: Long,
    updatedAt: Long,
)
