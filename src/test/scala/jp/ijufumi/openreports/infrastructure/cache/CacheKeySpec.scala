package jp.ijufumi.openreports.infrastructure.cache

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CacheKeySpec extends AnyFlatSpec with Matchers {

  "CacheKey.key" should "generate key without arguments" in {
    val key = CacheKeys.ApiToken.key()
    key should include("jp.ijufumi.openreports.infrastructure.cache.CacheKeys$ApiToken$")
  }

  it should "generate key with single argument" in {
    val key = CacheKeys.ApiToken.key("token123")
    key should include("jp.ijufumi.openreports.infrastructure.cache.CacheKeys$ApiToken$")
    key should include("token123")
  }

  it should "generate key with multiple arguments" in {
    val key = CacheKeys.GoogleAuthState.key("state1", "state2")
    key should include("jp.ijufumi.openreports.infrastructure.cache.CacheKeys$GoogleAuthState$")
    key should include("state1state2")
  }

  it should "generate unique keys for different cache types" in {
    val apiTokenKey = CacheKeys.ApiToken.key("test")
    val googleAuthStateKey = CacheKeys.GoogleAuthState.key("test")

    apiTokenKey should not equal googleAuthStateKey
  }

  it should "generate same key for same arguments" in {
    val key1 = CacheKeys.ApiToken.key("arg1", "arg2")
    val key2 = CacheKeys.ApiToken.key("arg1", "arg2")

    key1 should equal(key2)
  }
}
