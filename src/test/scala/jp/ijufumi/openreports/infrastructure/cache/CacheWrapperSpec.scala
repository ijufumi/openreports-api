package jp.ijufumi.openreports.infrastructure.cache

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import redis.clients.jedis.{Jedis, JedisPool}

import java.util.concurrent.ConcurrentHashMap

class CacheWrapperSpec extends AnyFlatSpec with Matchers {

  class MockJedis extends Jedis {
    private val storage = new ConcurrentHashMap[String, String]()

    override def setex(key: String, seconds: Long, value: String): String = {
      storage.put(key, value)
      "OK"
    }

    override def get(key: String): String = {
      storage.get(key)
    }

    override def del(key: String): Long = {
      if (storage.remove(key) != null) 1L else 0L
    }

    override def del(keys: String*): Long = {
      var count = 0L
      keys.foreach { key =>
        if (storage.remove(key) != null) count += 1
      }
      count
    }

    override def flushDB(): String = {
      storage.clear()
      "OK"
    }

    override def close(): Unit = {}
  }

  class MockJedisPool extends JedisPool {
    private val mockJedis = new MockJedis()
    override def getResource: Jedis = mockJedis
  }

  "CacheWrapper" should "be instantiable" in {
    noException should be thrownBy new CacheWrapper(new MockJedisPool())
  }

  "put" should "store value with default TTL" in {
    val cache = new CacheWrapper(new MockJedisPool())
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(Some("test-value"))
  }

  "put" should "store value with custom TTL" in {
    val cache = new CacheWrapper(new MockJedisPool())
    implicit val ttl: Long = 60
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(Some("test-value"))
  }

  "get" should "return None for non-existent key" in {
    val cache = new CacheWrapper(new MockJedisPool())
    val result = cache.get(CacheKeys.ApiToken, "non-existent-key")
    result should equal(None)
  }

  "remove" should "delete value from cache" in {
    val cache = new CacheWrapper(new MockJedisPool())
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    cache.remove(CacheKeys.ApiToken, "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(None)
  }

  "put" should "handle multiple arguments in cache key" in {
    val cache = new CacheWrapper(new MockJedisPool())
    cache.put(CacheKeys.ApiToken, "test-value", "arg1", "arg2", "arg3")
    val result = cache.get(CacheKeys.ApiToken, "arg1", "arg2", "arg3")
    result should equal(Some("test-value"))
  }

  "get" should "return None for different cache key arguments" in {
    val cache = new CacheWrapper(new MockJedisPool())
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key2")
    result should equal(None)
  }
}
