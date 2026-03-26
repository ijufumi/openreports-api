package jp.ijufumi.openreports.infrastructure.cache

import jp.ijufumi.openreports.configs.Config
import redis.clients.jedis.{JedisPool, JedisPoolConfig}

class CacheWrapper(
    pool: JedisPool = new JedisPool(new JedisPoolConfig(), Config.REDIS_HOST, Config.REDIS_PORT),
) {
  private val defaultTtl = Config.CACHE_TTL_SEC
  private val lock = new java.util.concurrent.locks.ReentrantReadWriteLock()

  def put(cacheKey: CacheKey, value: String, args: String*)(implicit
      ttl: Long = defaultTtl,
  ): Unit = {
    try {
      lock.writeLock().lock()
      val jedis = pool.getResource
      try {
        val key = cacheKey.key(args*)
        jedis.setex(key, ttl, value)
      } finally {
        jedis.close()
      }
    } finally {
      lock.writeLock().unlock()
    }
  }

  def get(cacheKey: CacheKey, args: String*): Option[String] = {
    try {
      lock.readLock().lock()
      val jedis = pool.getResource
      try {
        val value = jedis.get(cacheKey.key(args*))
        Option(value)
      } finally {
        jedis.close()
      }
    } finally {
      lock.readLock().unlock()
    }
  }

  def remove(cacheKey: CacheKey, args: String*): Unit = {
    try {
      lock.writeLock().lock()
      val jedis = pool.getResource
      try {
        jedis.del(cacheKey.key(args*))
      } finally {
        jedis.close()
      }
    } finally {
      lock.writeLock().unlock()
    }
  }

  def removeAll(): Unit = {
    try {
      lock.writeLock().lock()
      val jedis = pool.getResource
      try {
        jedis.flushDB()
      } finally {
        jedis.close()
      }
    } finally {
      lock.writeLock().unlock()
    }
  }
}
