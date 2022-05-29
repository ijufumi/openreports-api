package jp.ijufumi.openreports.service.enums

import scala.language.implicitConversions

object OutputType extends Enumeration {

  val XLS = NewVal("0")
  val PDF = NewVal("1")

  def of(value: String): OutputType.NewVal = {
    values.find(_.value == value).getOrElse(XLS)
  }

  implicit def valueToVal(x: Value): NewVal = x.asInstanceOf[NewVal]

  protected case class NewVal(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      NewVal.this == OutputType.of(value)
    }
  }

}
