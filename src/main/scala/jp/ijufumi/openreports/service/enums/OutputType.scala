package jp.ijufumi.openreports.service.enums

import scala.language.implicitConversions

object OutputType extends Enumeration {

  val XLS = Val("0")
  val PDF = Val("1")

  def of(value: String): OutputType.Val = {
    values.find(_.value == value).getOrElse(XLS)
  }

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  protected case class Val(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == OutputType.of(value)
    }
  }

}
