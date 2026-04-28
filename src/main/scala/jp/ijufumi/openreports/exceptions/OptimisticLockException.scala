package jp.ijufumi.openreports.exceptions

class OptimisticLockException(message: String = null, cause: Throwable = null)
    extends Exception(message, cause) {}
