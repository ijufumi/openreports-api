package jp.ijufumi.openreports.infrastructure.datastores.cache

sealed trait CacheKey {
  def key(args: String*): String = {
    s"${getClass.getName}-${args.mkString}"
  }
}

object CacheKeys {
  case object ApiToken extends CacheKey
  case object GoogleAuthState extends CacheKey
}
