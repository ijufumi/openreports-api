package jp.ijufumi.openreports

package object service {
  val PrefixClassPath = "classpath:"
  val TemplatePath =
    sys.env.getOrElse("TEMPLATE_PATH", PrefixClassPath + "report")
  val OutputFilePath = sys.env.getOrElse("OUTPUT_FILE_PATH", "report/output")
  val Hashedkey = "test"
}
