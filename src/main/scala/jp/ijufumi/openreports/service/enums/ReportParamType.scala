package jp.ijufumi.openreports.service.enums

import scala.beans.BeanProperty
import scala.language.implicitConversions

case class ReportParamType(@BeanProperty paramId: String,
                           @BeanProperty paramName: String) {}

object ReportParamType extends Enumeration {

  val TEXT = Val("1", "Text")
  val DATE = Val("2", "Date")
  val LIST = Val("3", "List")
  val QUERY = Val("4", "Query")

  def of(value: String): ReportParamType.Val = {
    values.find(_.paramName == value).getOrElse(TEXT)
  }

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  protected case class Val(paramId: String, paramName: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == ReportParamType.of(paramId)
    }
  }

  def list(): Array[ReportParamType] = {
    values.toSeq.sortBy(_.paramId).map(v => new ReportParamType(v.paramId, v.paramName)).toArray
  }
}
