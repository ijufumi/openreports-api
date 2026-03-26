package jp.ijufumi.openreports.presentation.controller.private_

import jp.ijufumi.openreports.domain.models.entity.{
  DataSource => DataSourceModel,
  Lists,
  Member => MemberModel,
}
import jp.ijufumi.openreports.usecase.port.input.{DataSourceUseCase, LoginUseCase}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class DataSourceServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginUseCase]
  val dataSourceService = mock[DataSourceUseCase]
  addServlet(new DataSourceServlet(loginService, dataSourceService), "/*")

  test("simple get") {
    val member = MemberModel("id", None, "email", "", "name", 0, 0)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (dataSourceService.getDataSources _)
      .expects("workspace-id")
      .returns(Lists(Seq.empty[DataSourceModel], 0, 0, 0))

    get("/", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should equal("{\"items\":[],\"offset\":0,\"limit\":0,\"count\":0}")
    }
  }

  test("missing workspace id header") {
    val member = MemberModel("id", None, "email", "", "name", 0, 0)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))

    get("/", headers = Map("Authorization" -> "api-token")) {
      status should equal(400)
    }
  }
}
