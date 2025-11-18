package jp.ijufumi.openreports.infrastructure.cache

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalacache._
import scalacache.modes.try_._

import scala.collection.mutable
import scala.concurrent.duration._
import scala.util.{Success, Try}

class CacheWrapperSpec extends AnyFlatSpec with Matchers {

  class MockCache extends Cache[String] {
    private val storage = mutable.Map[String, String]()

    def get[V](keyParts: Any*)(implicit config: CacheConfig, mode: Mode[Try], flags: Flags): Try[Option[V]] = {
      val key = keyParts.mkString(":")
      Success(storage.get(key).asInstanceOf[Option[V]])
    }

    def put[V](keyParts: Any*)(value: V, ttl: Option[Duration])(implicit config: CacheConfig, mode: Mode[Try], flags: Flags): Try[Unit] = {
      val key = keyParts.mkString(":")
      storage.put(key, value.asInstanceOf[String])
      Success(())
    }

    def remove(keyParts: Any*)(implicit mode: Mode[Try]): Try[Unit] = {
      val key = keyParts.mkString(":")
      storage.remove(key)
      Success(())
    }

    def removeAll()(implicit mode: Mode[Try]): Try[Unit] = {
      storage.clear()
      Success(())
    }

    def close()(implicit mode: Mode[Try]): Try[Unit] = {
      Success(())
    }
  }

  "CacheWrapper" should "be instantiable" in {
    implicit val mockCache: Cache[String] = new MockCache()
    noException should be thrownBy new CacheWrapper()
  }

  "put" should "store value with default TTL" in {
    implicit val mockCache: Cache[String] = new MockCache()
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(Some("test-value"))
  }

  "put" should "store value with custom TTL" in {
    implicit val mockCache: Cache[String] = new MockCache()
    val cache = new CacheWrapper()
    implicit val ttl: Long = 60
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(Some("test-value"))
  }

  "get" should "return None for non-existent key" in {
    implicit val mockCache: Cache[String] = new MockCache()
    val cache = new CacheWrapper()
    val result = cache.get(CacheKeys.ApiToken, "non-existent-key")
    result should equal(None)
  }

  "remove" should "delete value from cache" in {
    implicit val mockCache: Cache[String] = new MockCache()
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    cache.remove(CacheKeys.ApiToken, "key1")
    val result = cache.get(CacheKeys.ApiToken, "key1")
    result should equal(None)
  }

  "removeAll" should "clear all cache entries" in {
    implicit val mockCache: Cache[String] = new MockCache()
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "value1", "key1")
    cache.put(CacheKeys.GoogleAuthState, "value2", "key2")
    cache.removeAll()
    cache.get(CacheKeys.ApiToken, "key1") should equal(None)
    cache.get(CacheKeys.GoogleAuthState, "key2") should equal(None)
  }

  "put" should "handle multiple arguments in cache key" in {
    implicit val mockCache: Cache[String] = new MockCache()
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "test-value", "arg1", "arg2", "arg3")
    val result = cache.get(CacheKeys.ApiToken, "arg1", "arg2", "arg3")
    result should equal(Some("test-value"))
  }

  "get" should "return None for different cache key arguments" in {
    implicit val mockCache: Cache[String] = new MockCache()
    val cache = new CacheWrapper()
    cache.put(CacheKeys.ApiToken, "test-value", "key1")
    val result = cache.get(CacheKeys.ApiToken, "key2")
    result should equal(None)
  }
}
