package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class UpdateReport(name: String, templateId: String, dataSourceId: Option[String])

object UpdateReport {
  implicit val validate: Validator[UpdateReport] = new Validator[UpdateReport] {
    def validate(param: UpdateReport) = collectViolations(
      notEmpty("name", param.name),
      lengthBetween("name", param.name, 1, 20),
      notEmpty("templateId", param.templateId),
    )
  }
}
