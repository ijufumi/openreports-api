package jp.ijufumi.openreports.infrastructure.cache

import jp.ijufumi.openreports.configs.Config
import org.slf4j.LoggerFactory
import redis.clients.jedis.{JedisPool, JedisPoolConfig}

class CacheWrapper(
    pool: JedisPool = {
      val config = new JedisPoolConfig()
      config.setMaxTotal(Config.REDIS_POOL_MAX_SIZE)
      config.setMaxIdle(Config.REDIS_POOL_MAX_IDLE)
      config.setMinIdle(Config.REDIS_POOL_MIN_IDLE)
      config.setTestOnBorrow(true)
      new JedisPool(config, Config.REDIS_HOST, Config.REDIS_PORT, 3000)
    },
) {
  private val logger = LoggerFactory.getLogger(getClass)
  private val defaultTtl = Config.CACHE_TTL_SEC

  def put(cacheKey: CacheKey, value: String, args: String*)(implicit
      ttl: Long = defaultTtl,
  ): Unit = {
    withJedis(s"put $cacheKey") { jedis =>
      jedis.setex(cacheKey.key(args*), ttl, value)
      ()
    }
  }

  def get(cacheKey: CacheKey, args: String*): Option[String] = {
    withJedis(s"get $cacheKey") { jedis =>
      Option(jedis.get(cacheKey.key(args*)))
    }.flatten
  }

  def remove(cacheKey: CacheKey, args: String*): Unit = {
    withJedis(s"remove $cacheKey") { jedis =>
      jedis.del(cacheKey.key(args*))
      ()
    }
  }

  private def withJedis[T](op: String)(f: redis.clients.jedis.Jedis => T): Option[T] = {
    try {
      val jedis = pool.getResource
      try Some(f(jedis))
      finally jedis.close()
    } catch {
      case e: Exception =>
        logger.warn(s"cache operation failed: $op", e)
        None
    }
  }
}
