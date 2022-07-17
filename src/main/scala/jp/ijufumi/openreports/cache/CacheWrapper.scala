package jp.ijufumi.openreports.cache

import jp.ijufumi.openreports.config.Config
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

class CacheWrapper {
  private val ttl = Config.CACHE_TTL_SEC
  private val cache: Cache[Any] = CaffeineCache[Any]

  def put[T](key: String, value: T): Unit = {
    cache.put(key)(value, ttl = ttl)
  }

  def get[T](key: String): Option[T] = {
    val value = cache.get(key)
    if (value.isFailure) {
      return None
    }
    val original = value.get
    Option.apply(original.asInstanceOf[T])
  }

  def remove(key: String): Unit = {
    cache.remove(key)
  }

  def removeAll(): Unit = {
    cache.removeAll()
  }
}
