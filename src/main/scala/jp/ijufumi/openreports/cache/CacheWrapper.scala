package jp.ijufumi.openreports.cache

import jp.ijufumi.openreports.config.Config
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

import scala.concurrent.duration._

class CacheWrapper {
  private val defaultTtl = Config.CACHE_TTL_SEC
  private val cache: Cache[Any] = CaffeineCache[Any]

  def put[T](key: CacheKey, value: T, ttl: Long = defaultTtl): Unit = {
    cache.put(key)(value, ttl = Option(Duration(ttl, SECONDS)))
  }

  def get[T](key: CacheKey): Option[T] = {
    val value = cache.get(key)
    if (value.isFailure) {
      return None
    }
    val original = value.get
    Option.apply(original.asInstanceOf[T])
  }

  def remove(key: CacheKey): Unit = {
    cache.remove(key)
  }

  def removeAll(): Unit = {
    cache.removeAll()
  }
}
