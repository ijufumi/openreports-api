package jp.ijufumi.openreports.service.enums

import scala.language.implicitConversions

object StatusCode extends Enumeration {

  val OK = Val("")
  val DATA_NOT_FOUND = Val("not found")
  val DUPLICATE_ERR = Val("duplicate key value violates")
  val OTHER_ERROR = Val("error")

  def of(e: Exception): StatusCode.Val = {
    of(e.getMessage)
  }

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  def of(value: String): StatusCode.Val = {
    values.find(_.value.contains(value)).getOrElse(OTHER_ERROR)
  }

  protected case class Val(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == StatusCode.of(value)
    }
  }

}
