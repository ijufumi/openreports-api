package jp.ijufumi.openreports.infrastructure.datastores.cache

import jp.ijufumi.openreports.configs.Config
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

import scala.concurrent.duration._

class CacheWrapper {
  private val defaultTtl = Config.CACHE_TTL_SEC
  private val cache: Cache[Any] = CaffeineCache[Any]

  def put[T](cacheKey: CacheKey, value: T, args: String*)(implicit ttl: Long = defaultTtl): Unit = {
    cache.put(cacheKey.key(args: _*))(value, ttl = Some(Duration(ttl, SECONDS)))
  }

  def get[T](cacheKey: CacheKey, args: String*): Option[T] = {
    val value = cache.get(cacheKey.key(args: _*))
    if (value.isFailure || value.get.isEmpty) {
      return None
    }
    val original = value.get.get
    Some(original.asInstanceOf[T])
  }

  def getAsSeq[T](cacheKey: CacheKey, args: String*): Option[Seq[T]] = {
    val value = cache.get(cacheKey.key(args: _*))
    if (value.isFailure || value.get.isEmpty) {
      return None
    }
    val original = value.get.get
    Some(original.asInstanceOf[Seq[T]])
  }

  def add[T](cacheKey: CacheKey, value: T, args: String*)(implicit ttl: Long = defaultTtl): Unit = {
    val v = get[Seq[T]](cacheKey, args: _*)
    if (v.isDefined) {
      val vv = v.get
      vv :+ value
      put(cacheKey, vv, args: _*)
    } else {
      val vv = Seq(value)
      put(cacheKey, vv, args: _*)
    }
  }

  def remove(cacheKey: CacheKey, args: String*): Unit = {
    cache.remove(cacheKey.key(args: _*))
  }

  def removeAll(): Unit = {
    cache.removeAll()
  }
}
