package jp.ijufumi.openreports

import slick.ast.ScalaBaseType

import java.sql.Timestamp

package object entities {
  implicit val timestampType: ScalaBaseType[Timestamp] = new ScalaBaseType[Timestamp]
}
