package jp.ijufumi.openreports.infrastructure.external.google.impl

import jp.ijufumi.openreports.infrastructure.external.google.models.{AccessToken, UserInfo}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import sttp.client4._
import sttp.client4.testing.SyncBackendStub
import sttp.model.{RequestMetadata, StatusCode, Method, Uri}

class GoogleRepositoryImplSpec extends AnyFlatSpec with Matchers {
  private implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
  private implicit val serialization: Serialization.type = org.json4s.native.Serialization

  "GoogleRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new GoogleRepositoryImpl()
  }

  "getAuthorizationUrl" should "return valid authorization URL" in {
    val repository = new GoogleRepositoryImpl()
    val url = repository.getAuthorizationUrl()

    url should include("https://accounts.google.com/o/oauth2/auth")
    url should include("client_id")
    url should include("response_type=code")
    url should include("state")
    url should include("scope")
    url should include("redirect_uri")
  }

  it should "include required scopes in authorization URL" in {
    val repository = new GoogleRepositoryImpl()
    val url = repository.getAuthorizationUrl()

    url should include("profile")
    url should include("email")
  }

  it should "generate different states for each call" in {
    val repository = new GoogleRepositoryImpl()
    val url1 = repository.getAuthorizationUrl()
    val url2 = repository.getAuthorizationUrl()

    url1 should not equal url2
  }

  "fetchToken" should "return access token for valid code" in {
    val accessToken = AccessToken(
      access_token = "test-access-token",
    )

    val responseJson = Serialization.write(accessToken)

    val backendStub = mock[WebSocketSyncBackend]
    when(backendStub.send(any[Request[String]])).thenReturn(makeResponse(responseJson, StatusCode.Ok))

    val repository = new GoogleRepositoryImpl(backendStub)
    val token = repository.fetchToken("valid-code")

    token should be(defined)
    token.get should equal("test-access-token")
  }

  it should "return None for invalid code" in {
    val backendStub = mock[WebSocketSyncBackend]
    when(backendStub.send(any[Request[String]])).thenReturn(makeResponse("", StatusCode.BadRequest))

    val repository = new GoogleRepositoryImpl(backendStub)
    val token = repository.fetchToken("invalid-code")

    token should be(None)
  }

  it should "return None when response body is invalid JSON" in {
    val backendStub = mock[WebSocketSyncBackend]
    when(backendStub.send(any[Request[String]])).thenReturn(makeResponse("invalid json", StatusCode.Ok))

    val repository = new GoogleRepositoryImpl(backendStub)
    val token = repository.fetchToken("valid-code")

    token should be(None)
  }

  "getUserInfo" should "return user info for valid access token" in {
    val userInfo = UserInfo(
      id = "12345",
      email = "test@example.com",
      name = "Test User",
      picture = "https://example.com/picture.jpg"
    )

    val responseJson = Serialization.write(userInfo)

    val backendStub = mock[WebSocketSyncBackend]
    when(backendStub.send(any[Request[String]])).thenReturn(makeResponse(responseJson, StatusCode.Ok))

    val repository = new GoogleRepositoryImpl(backendStub)
    val result = repository.getUserInfo("valid-access-token")

    result should be(defined)
    result.get.email should equal("test@example.com")
    result.get.name should equal("Test User")
  }

  it should "return None for invalid access token" in {
    val backendStub = mock[WebSocketSyncBackend]
    when(backendStub.send(any[Request[String]])).thenReturn(makeResponse("", StatusCode.Unauthorized))

    val repository = new GoogleRepositoryImpl(backendStub)
    val userInfo = repository.getUserInfo("invalid-access-token")

    userInfo should be(None)
  }

  it should "return None when response body is invalid JSON" in {
    val backendStub = mock[WebSocketSyncBackend]
    when(backendStub.send(any[Request[String]])).thenReturn(makeResponse("invalid json", StatusCode.Ok))

    val repository = new GoogleRepositoryImpl(backendStub)
    val userInfo = repository.getUserInfo("valid-access-token")

    userInfo should be(None)
  }

  def makeResponse (body: String, code: StatusCode): Response[String] = {
    Response(body, code, RequestMetadata(Method.POST, Uri("http://test.jp"), scala.collection.immutable.Seq.empty))
  }
}
