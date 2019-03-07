package jp.ijufumi.openreports.vo
import scala.beans.BeanProperty

case class ReportParamConfigInfo(
  @BeanProperty paramId: Long,
  @BeanProperty pageNo: Int,
  @BeanProperty seq: Int
)
