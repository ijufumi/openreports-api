package jp.ijufumi.openreports.presentation.request

import com.wix.accord._
import com.wix.accord.dsl._

case class CreateReport(
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    parameterIds: Seq[String] = Seq.empty,
)

object CreateReport {
  implicit val validate: Validator[CreateReport] = validator[CreateReport] { param =>
    param.name is notEmpty
    param.name.length is between(1, 20)

    param.templateId is notEmpty
  }
}
