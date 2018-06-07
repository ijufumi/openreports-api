package jp.ijufumi.openreports

package object service {
  val prefixClassPath = "classpath:"
  val templatePath = sys.env.getOrElse("TEMPLATE_PATH", prefixClassPath + "report")
}
