package jp.ijufumi.openreports.utils

import org.slf4j.{Logger, LoggerFactory}

trait Logging {
  protected val logger: Logger = LoggerFactory.getLogger(getClass);
}
