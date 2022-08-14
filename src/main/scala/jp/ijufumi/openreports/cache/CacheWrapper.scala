package jp.ijufumi.openreports.cache

import jp.ijufumi.openreports.config.Config
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

import scala.concurrent.duration._

class CacheWrapper {
  private val defaultTtl = Config.CACHE_TTL_SEC
  private val cache: Cache[Any] = CaffeineCache[Any]

  def put[T](cacheKey: CacheKey, value: T, args: String*)(implicit ttl: Long = defaultTtl): Unit = {
    cache.put(cacheKey.key(args: _*))(value, ttl = Option(Duration(ttl, SECONDS)))
  }

  def get[T](cacheKey: CacheKey, args: String*): Option[T] = {
    val value = cache.get(cacheKey.key(args: _*))
    if (value.isFailure) {
      return None
    }
    val original = value.get.get
    Option(original.asInstanceOf[T])
  }

  def remove(cacheKey: CacheKey, args: String*): Unit = {
    cache.remove(cacheKey.key(args: _*))
  }

  def removeAll(): Unit = {
    cache.removeAll()
  }
}
