package jp.ijufumi.openreports.cache

import jp.ijufumi.openreports.config.Config
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

class CacheWrapper {
  private val defaultTtl = Config.CACHE_TTL_SEC
  private val cache: Cache[Any] = CaffeineCache[Any]

  def put[T](key: CacheKey, value: T, ttl: Integer = defaultTtl): Unit = {
    cache.put(key)(value, ttl = ttl)
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
