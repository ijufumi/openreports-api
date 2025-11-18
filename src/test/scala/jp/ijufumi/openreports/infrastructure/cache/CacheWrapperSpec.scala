package jp.ijufumi.openreports.infrastructure.cache

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CacheWrapperSpec extends AnyFlatSpec with Matchers {

  "CacheWrapper" should "be instantiable" in {
    // Note: This is a basic test that verifies the CacheWrapper can be instantiated.
    // Full integration tests require a running Redis instance and are typically
    // performed in integration test suites.
    noException should be thrownBy new CacheWrapper()
  }

  // Note: The following tests would require a Redis mock or a running Redis instance.
  // They are commented out as they depend on external infrastructure.
  // For proper testing, consider:
  // 1. Using an embedded Redis for integration tests
  // 2. Creating a trait for CacheWrapper with injectable Cache[String]
  // 3. Moving to integration tests that run against a real Redis instance

  /*
  "put" should "store value with default TTL" in {
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(Some("test-value"))
  }

  "put" should "store value with custom TTL" in {
    val cache = new CacheWrapper()
    implicit val ttl: Long = 60
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(Some("test-value"))
  }

  "get" should "return None for non-existent key" in {
    val cache = new CacheWrapper()
    val result = cache.get(CacheKeys.ApiToken, "non-existent-key")
    result should equal(None)
  }

  "remove" should "delete value from cache" in {
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    cache.remove(CacheKeys.ApiToken, "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(None)
  }

  "removeAll" should "clear all cache entries" in {
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "value1", "key1")
    cache.put(CacheKeys.GoogleAuthState, "value2", "key2")
    cache.removeAll()
    cache.get(CacheKeys.ApiToken, "key1") should equal(None)
    cache.get(CacheKeys.GoogleAuthState, "key2") should equal(None)
  }
  */
}
