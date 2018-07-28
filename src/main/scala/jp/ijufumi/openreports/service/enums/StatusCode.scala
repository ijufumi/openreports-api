package jp.ijufumi.openreports.service.enums

import scala.language.implicitConversions

object StatusCode extends Enumeration {
  protected case class Val(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == StatusCode.of(value)
    }
  }

  val OK = Val("")
  val DUPLICATE_ERR = Val("duplicate key value violates")
  val OTHER_ERROR = Val("error")

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  def of(e: Exception): StatusCode.Val = {
    of(e.getMessage)
  }

  def of(value: String): StatusCode.Val = {
    values.find(_.value.contains(value)).getOrElse(OTHER_ERROR)
  }

}
