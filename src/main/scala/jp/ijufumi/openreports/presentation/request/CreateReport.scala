package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class CreateReport(
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    parameterIds: Seq[String] = Seq.empty,
)

object CreateReport {
  implicit val validate: Validator[CreateReport] = new Validator[CreateReport] {
    def validate(param: CreateReport) = collectViolations(
      notEmpty("name", param.name),
      lengthBetween("name", param.name, 1, 20),
      notEmpty("templateId", param.templateId),
    )
  }
}
