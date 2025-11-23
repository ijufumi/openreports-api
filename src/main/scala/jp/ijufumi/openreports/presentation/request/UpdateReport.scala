package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class UpdateReport(name: String, templateId: String, dataSourceId: Option[String])

object UpdateReport {
  implicit val validate: Validator[UpdateReport] = validator[UpdateReport] { param =>
    param.name is notEmpty
    param.name.length is between(1, 20)
    param.templateId is notEmpty
  }
}
