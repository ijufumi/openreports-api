package jp.ijufumi.openreports

package object service {
  val PREFIX_CLASS_PATH = "classpath:"
  val TEMPLATE_PATH = sys.env.getOrElse("TEMPLATE_PATH", PREFIX_CLASS_PATH + "report")
  val OUTPUT_FILE_PATH = sys.env.getOrElse("OUTPUT_FILE_PATH", "report/output")
}
