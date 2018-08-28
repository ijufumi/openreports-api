package jp.ijufumi.openreports.controller.common

import java.io.{BufferedInputStream, FileInputStream, InputStream}
import java.time.{LocalDateTime, ZoneOffset}

import jp.ijufumi.openreports.controller.PublicPath
import skinny._
import skinny.controller.feature.ThymeleafTemplateEngineFeature
import skinny.filter._

import scala.util.control.Breaks

/**
  * The base jp.ijufumi.openreports.controller for this Skinny application.
  *
  * see also "http://skinny-framework.org/documentation/jp.ijufumi.openreports.controller-and-routes.html"
  */
trait ApplicationController
    extends SkinnyController
    // with TxPerRequestFilter
    with SkinnySessionFilter
    with ErrorPageFilter
    with I18nFeature
    with ControllerCommonFeature
    with ThymeleafTemplateEngineFeature {
}
