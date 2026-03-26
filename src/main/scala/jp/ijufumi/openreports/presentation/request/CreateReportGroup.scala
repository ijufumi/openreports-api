package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class CreateReportGroup(name: String, reportIds: Seq[String])

object CreateReportGroup {
  implicit val validate: Validator[CreateReportGroup] = new Validator[CreateReportGroup] {
    def validate(param: CreateReportGroup) = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
    )
  }
}
