package jp.ijufumi.openreports.infrastructure.datastores.cache

import jp.ijufumi.openreports.configs.Config
import scalacache._
import scalacache.modes.try_._
import scalacache.redis._

import scala.concurrent.duration._

class CacheWrapper {
  private val defaultTtl = Config.CACHE_TTL_SEC
  private val cache: Cache[String] =
    RedisCache(Config.REDIS_HOST, Config.REDIS_PORT)
  private val lock = new java.util.concurrent.locks.ReentrantReadWriteLock()

  def put(cacheKey: CacheKey, value: String, args: String*)(implicit
      ttl: Long = defaultTtl,
  ): Unit = {
    try {
      lock.writeLock().lock()
      cache.put(cacheKey.key(args: _*))(value, ttl = Some(Duration(ttl, SECONDS)))
    } finally {
      lock.writeLock().unlock()
    }
  }

  def get(cacheKey: CacheKey, args: String*): Option[String] = {
    try {
      lock.readLock().lock()
      val value = cache.get(cacheKey.key(args: _*))
      if (value.isFailure || value.get.isEmpty) {
        return None
      }
      val original = value.get.get
      Some(original.asInstanceOf[T])
    } finally {
      lock.readLock().unlock()
    }
  }

  def remove(cacheKey: CacheKey, args: String*): Unit = {
    try {
      lock.writeLock().lock()
      cache.remove(cacheKey.key(args: _*))
    } finally {
      lock.writeLock().unlock()
    }
  }

  def removeAll(): Unit = {
    try {
      lock.writeLock().lock()
      cache.removeAll()
    } finally {
      lock.writeLock().unlock()
    }
  }
}
