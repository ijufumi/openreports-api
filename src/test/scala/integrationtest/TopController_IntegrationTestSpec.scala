package integrationtest

import jp.ijufumi.openreports.controller.Controllers
import skinny.test.{SkinnyFlatSpec, SkinnyTestSupport}

class TopController_IntegrationTestSpec extends SkinnyFlatSpec with SkinnyTestSupport {
  Controllers.top$.mount(servletContextHandler.getServletContext)

  it should "show top page" in {
    get("/") {
      status should equal(200)
    }
  }

}
