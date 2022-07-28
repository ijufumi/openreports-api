package jp.ijufumi.openreports.cache

sealed trait CacheKey {
  def key(args: String*): String = {
    s"${getClass.getName}-${Array.from(args).mkString}"
  }
}

object CacheKeys {
  case object ApiToken extends CacheKey
  case object GoogleAuthState extends CacheKey
}
