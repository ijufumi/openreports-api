package jp.ijufumi.openreports.gateways.datastores.database.entities

import jp.ijufumi.openreports.interfaces.models.inputs.UpdateReport
import jp.ijufumi.openreports.interfaces.models.value.enums.EmbeddedFunctionTypes.EmbeddedFunctionType
import jp.ijufumi.openreports.interfaces.models.value.enums.ParameterTypes.ParameterType
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates

case class ReportParameter(
    id: String,
    workspaceId: String,
    parameterType: ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionType],
    value: Option[String],
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class ReportParameters(tag: Tag)
    extends EntityBase[ReportParameter](
      tag,
      "report_parameters",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def workspaceId = column[String]("workspace_id")
  def parameterType = column[ParameterType]("parameter_type")
  def embeddedFunctionType = column[Option[EmbeddedFunctionType]]("embedded_function_type")
  def value = column[Option[String]]("value")

  override def * =
    (
      id,
      workspaceId,
      parameterType,
      embeddedFunctionType,
      value,
      createdAt,
      updatedAt,
      versions,
    ) <> (ReportParameter.tupled, ReportParameter.unapply)
}
