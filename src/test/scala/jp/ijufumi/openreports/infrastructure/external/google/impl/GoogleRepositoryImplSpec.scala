package jp.ijufumi.openreports.infrastructure.external.google.impl

import jp.ijufumi.openreports.infrastructure.external.google.models.{AccessToken, UserInfo}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client4._
import sttp.client4.testing.SyncBackendStub
import sttp.model.StatusCode

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
      expires_in = 3600,
      token_type = "Bearer",
      scope = "profile email",
      refresh_token = Some("test-refresh-token")
    )

    val responseJson = Serialization.write(accessToken)

    val backendStub = SyncBackendStub
      .whenRequestMatches(_ => true)
      .thenRespond(responseJson, StatusCode.Ok)

    val repository = new GoogleRepositoryImpl(backendStub)
    val token = repository.fetchToken("valid-code")

    token should be(defined)
    token.get should equal("test-access-token")
  }

  it should "return None for invalid code" in {
    val backendStub = SyncBackendStub
      .whenRequestMatches(_ => true)
      .thenRespond("", StatusCode.BadRequest)

    val repository = new GoogleRepositoryImpl(backendStub)
    val token = repository.fetchToken("invalid-code")

    token should be(None)
  }

  it should "return None when response body is invalid JSON" in {
    val backendStub = SyncBackendStub
      .whenRequestMatches(_ => true)
      .thenRespond("invalid json", StatusCode.Ok)

    val repository = new GoogleRepositoryImpl(backendStub)
    val token = repository.fetchToken("valid-code")

    token should be(None)
  }

  "getUserInfo" should "return user info for valid access token" in {
    val userInfo = UserInfo(
      id = "12345",
      email = "test@example.com",
      verified_email = true,
      name = "Test User",
      given_name = Some("Test"),
      family_name = Some("User"),
      picture = Some("https://example.com/picture.jpg")
    )

    val responseJson = Serialization.write(userInfo)

    val backendStub = SyncBackendStub
      .whenRequestMatches(_ => true)
      .thenRespond(responseJson, StatusCode.Ok)

    val repository = new GoogleRepositoryImpl(backendStub)
    val result = repository.getUserInfo("valid-access-token")

    result should be(defined)
    result.get.email should equal("test@example.com")
    result.get.name should equal("Test User")
  }

  it should "return None for invalid access token" in {
    val backendStub = SyncBackendStub
      .whenRequestMatches(_ => true)
      .thenRespond("", StatusCode.Unauthorized)

    val repository = new GoogleRepositoryImpl(backendStub)
    val userInfo = repository.getUserInfo("invalid-access-token")

    userInfo should be(None)
  }

  it should "return None when response body is invalid JSON" in {
    val backendStub = SyncBackendStub
      .whenRequestMatches(_ => true)
      .thenRespond("invalid json", StatusCode.Ok)

    val repository = new GoogleRepositoryImpl(backendStub)
    val userInfo = repository.getUserInfo("valid-access-token")

    userInfo should be(None)
  }
}
