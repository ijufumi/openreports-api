package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class UpdateReportGroup(name: String, reportIds: Seq[String])

object UpdateReportGroup {
  implicit val validate: Validator[UpdateReportGroup] = new Validator[UpdateReportGroup] {
    def validate(param: UpdateReportGroup) = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
    )
  }
}
