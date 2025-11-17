package jp.ijufumi.openreports.infrastructure.external.google.impl

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GoogleRepositoryImplSpec extends AnyFlatSpec with Matchers {

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

  // Note: The following tests require mocking HTTP client or actual API calls
  // They should be implemented as integration tests with proper mocking framework
  // or test against a mock server

  /*
  "fetchToken" should "return access token for valid code" in {
    val repository = new GoogleRepositoryImpl()
    val token = repository.fetchToken("valid-code")
    token should be(defined)
  }

  it should "return None for invalid code" in {
    val repository = new GoogleRepositoryImpl()
    val token = repository.fetchToken("invalid-code")
    token should be(None)
  }

  "getUserInfo" should "return user info for valid access token" in {
    val repository = new GoogleRepositoryImpl()
    val userInfo = repository.getUserInfo("valid-access-token")
    userInfo should be(defined)
    userInfo.get.email should not be empty
  }

  it should "return None for invalid access token" in {
    val repository = new GoogleRepositoryImpl()
    val userInfo = repository.getUserInfo("invalid-access-token")
    userInfo should be(None)
  }
  */
}
