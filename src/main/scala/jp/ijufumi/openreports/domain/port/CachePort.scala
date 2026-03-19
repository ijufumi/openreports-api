package jp.ijufumi.openreports.domain.port

sealed trait CacheKey {
  def key(args: String*): String = {
    s"${getClass.getName}-${args.mkString}"
  }
}

object CacheKeys {
  case object ApiToken extends CacheKey
  case object GoogleAuthState extends CacheKey
}

trait CachePort {
  def put(cacheKey: CacheKey, value: String, args: String*)(implicit ttl: Long): Unit

  def get(cacheKey: CacheKey, args: String*): Option[String]

  def remove(cacheKey: CacheKey, args: String*): Unit
}
