package jp.ijufumi.openreports.service.enums

import scala.beans.BeanProperty
import scala.language.implicitConversions

case class ReportParamType(@BeanProperty paramId: String,
                           @BeanProperty paramName: String) {}

object ReportParamType extends Enumeration {

  val TEXT = NewVal("1", "Text")
  val DATE = NewVal("2", "Date")
  val LIST = NewVal("3", "List")
  val QUERY = NewVal("4", "Query")

  def of(value: String): ReportParamType.Val = {
    values.find(_.paramName == value).getOrElse(TEXT)
  }

  implicit def valueToVal(x: Value): NewVal = x.asInstanceOf[NewVal]

  protected case class NewVal(paramId: String, paramName: String) extends super.Val {
    def equals(value: String): Boolean = {
      NewVal.this == ReportParamType.of(paramId)
    }
  }

  def list(): Array[ReportParamType] = {
    values.toSeq.sortBy(_.paramId).map(v => new ReportParamType(v.paramId, v.paramName)).toArray
  }
}
