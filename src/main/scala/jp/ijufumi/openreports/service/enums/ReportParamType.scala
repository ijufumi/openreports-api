package jp.ijufumi.openreports.service.enums

import scala.language.implicitConversions

object ReportParamType extends Enumeration {

  protected case class Val(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == ReportParamType.of(value)
    }
  }

  val TEXT = Val("1")
  val DATE = Val("2")
  val LIST = Val("3")
  val QUERY = Val("4")

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  def of(value: String): ReportParamType.Val = {
    values.find(_.value == value).getOrElse(TEXT)
  }
}
