package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class CreateReportGroup(name: String, reportIds: Seq[String])

object CreateReportGroup {
  implicit val validate: Validator[CreateReportGroup] = validator[CreateReportGroup] { param =>
    param.name is notEmpty
    param.name.length is between(1, 255)
  }
}
