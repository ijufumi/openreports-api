package jp.ijufumi.openreports.controller

import org.scalatest._
import skinny.test.MockController

class TopControllerSpec extends FunSpec with Matchers {

  describe("RootController") {
    it("shows top page") {
      val controller = new TopController with MockController
      controller.index
      controller.contentType should equal("text/html; charset=utf-8")
      controller.renderCall.map(_.path) should equal(Some("/top/index"))
    }
  }

}
