package jp.ijufumi.openreports.infrastructure.datastores.cache

import jp.ijufumi.openreports.configs.Config
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

import scala.concurrent.duration._

class CacheWrapper {
  private val defaultTtl = Config.CACHE_TTL_SEC
  private val cache: Cache[Any] = CaffeineCache[Any]
  private val lock = new java.util.concurrent.locks.ReentrantReadWriteLock()

  def put[T](cacheKey: CacheKey, value: T, args: String*)(implicit ttl: Long = defaultTtl): Unit = {
    try {
      lock.writeLock().lock()
      cache.put(cacheKey.key(args: _*))(value, ttl = Some(Duration(ttl, SECONDS)))
    } finally {
      lock.writeLock().unlock()
    }
  }

  def get[T](cacheKey: CacheKey, args: String*): Option[T] = {
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

  def getAsSeq[T](cacheKey: CacheKey, args: String*): Option[Seq[T]] = {
    try {
      lock.readLock().lock()
      val value = cache.get(cacheKey.key(args: _*))
      if (value.isFailure || value.get.isEmpty) {
        return None
      }
      val original = value.get.get
      Some(original.asInstanceOf[Seq[T]])
    } finally {
      lock.readLock().unlock()
    }
  }

  def add[T](cacheKey: CacheKey, value: T, args: String*)(implicit
      ttl: Long = defaultTtl,
      limitation: Int = 100,
  ): Unit = {
    try {
      lock.writeLock().lock()
      val v = get[Seq[T]](cacheKey, args: _*)
      if (v.isDefined) {
        var vv = v.get
        vv :+ value
        if (vv.length > limitation) {
          vv = vv.slice(vv.length - limitation, vv.length)
        }
        put(cacheKey, vv, args: _*)
      } else {
        val vv = Seq(value)
        put(cacheKey, vv, args: _*)
      }
    } finally {
      lock.writeLock().unlock()
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
