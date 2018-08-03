package jp.ijufumi.openreports.service.enums

import scala.language.implicitConversions

object ReportParamType extends Enumeration {

  val TEXT = Val("1")
  val DATE = Val("2")
  val LIST = Val("3")
  val QUERY = Val("4")

  def of(value: String): ReportParamType.Val = {
    values.find(_.value == value).getOrElse(TEXT)
  }

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  protected case class Val(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == ReportParamType.of(value)
    }
  }

}
