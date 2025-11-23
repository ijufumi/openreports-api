package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class UpdateReportGroup(name: String, reportIds: Seq[String])

object UpdateReportGroup {
  implicit val validate: Validator[UpdateReportGroup] = validator[UpdateReportGroup] { param =>
    param.name is notEmpty
    param.name.length is between(1, 255)
  }
}
