package jp.ijufumi.openreports.controller

import skinny._
import skinny.filter._

/**
 * The base jp.ijufumi.openreports.controller for this Skinny application.
 *
 * see also "http://skinny-framework.org/documentation/jp.ijufumi.openreports.controller-and-routes.html"
 */
trait ApplicationController extends SkinnyController
    // with TxPerRequestFilter
    with SkinnySessionFilter
    with ErrorPageFilter {

  // override def defaultLocale = Some(new java.util.Locale("ja"))

}
