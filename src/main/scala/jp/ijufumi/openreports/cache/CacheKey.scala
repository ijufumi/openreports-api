package jp.ijufumi.openreports.cache

sealed trait CacheKey

object CacheKeys {
  case object ApiToken extends CacheKey
  case object GoogleAuthState extends CacheKey
}
