package jp.ijufumi.openreports
import skinny.SkinnyEnv

package object controller {
  val PublicPath = "/public"
  val PrivatePath = "/private"
  val ViewRootPath = if (SkinnyEnv.isTest()) "views/" else "/views/"
  val ViewPublicPath = ViewRootPath + "/public"
  val ViewPrivatePath = ViewRootPath + "/private"
}
