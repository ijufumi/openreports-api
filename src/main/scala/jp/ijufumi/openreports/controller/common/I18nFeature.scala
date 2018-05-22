package jp.ijufumi.openreports.controller.common

import skinny.I18n

trait I18nFeature {
  def i18n: I18n = new I18n()
}
