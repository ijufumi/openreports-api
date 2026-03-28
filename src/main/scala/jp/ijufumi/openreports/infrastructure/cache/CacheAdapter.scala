package jp.ijufumi.openreports.infrastructure.cache

import com.google.inject.Inject
import jp.ijufumi.openreports.domain.port.CachePort

class CacheAdapter @Inject() (cacheWrapper: CacheWrapper) extends CachePort {
  override def put(
      cacheKey: jp.ijufumi.openreports.domain.port.CacheKey,
      value: String,
      args: String*,
  )(implicit ttl: Long): Unit = {
    val infraKey = toInfraCacheKey(cacheKey)
    cacheWrapper.put(infraKey, value, args: _*)(ttl)
  }

  override def get(
      cacheKey: jp.ijufumi.openreports.domain.port.CacheKey,
      args: String*,
  ): Option[String] = {
    val infraKey = toInfraCacheKey(cacheKey)
    cacheWrapper.get(infraKey, args: _*)
  }

  override def remove(
      cacheKey: jp.ijufumi.openreports.domain.port.CacheKey,
      args: String*,
  ): Unit = {
    val infraKey = toInfraCacheKey(cacheKey)
    cacheWrapper.remove(infraKey, args: _*)
  }

  private def toInfraCacheKey(cacheKey: jp.ijufumi.openreports.domain.port.CacheKey): CacheKey = {
    cacheKey match {
      case _: jp.ijufumi.openreports.domain.port.CacheKeys.ApiToken.type => CacheKeys.ApiToken
      case _: jp.ijufumi.openreports.domain.port.CacheKeys.GoogleAuthState.type =>
        CacheKeys.GoogleAuthState
    }
  }
}
