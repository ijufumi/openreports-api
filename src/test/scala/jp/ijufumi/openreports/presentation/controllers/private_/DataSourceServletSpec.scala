package jp.ijufumi.openreports.presentation.controllers.private_

import jp.ijufumi.openreports.presentation.models.responses.{DataSource, Lists, Member}
import jp.ijufumi.openreports.services.{DataSourceService, LoginService}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._
import org.scalatra.ActionResult

class DataSourceServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginService]
  val dataSourceService = mock[DataSourceService]
  addServlet(new DataSourceServlet(loginService, dataSourceService), "/*")

  test("simple get") {
    val member = Member("id", "name", "email", Seq.empty)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (dataSourceService.getDataSources _).expects("workspace-id").returns(Lists(Seq.empty[DataSource], 0, 0, 0))
    get("/", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should equal("{\"items\":[],\"offset\":0,\"limit\":0,\"count\":0}")
    }
  }
}

