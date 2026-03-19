package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.{EmbeddedFunctionTypes, ParameterTypes}
import jp.ijufumi.openreports.utils.Dates

case class ReportParameter(
    id: String,
    workspaceId: String,
    parameterType: ParameterTypes.ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionTypes.EmbeddedFunctionType],
    value: Option[String],
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)
