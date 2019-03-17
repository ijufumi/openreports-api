package jp.ijufumi.openreports.vo
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import skinny.micro.implicits.TypeConverter
import org.json4s.jackson.Serialization.read

trait Converters {
  implicit val notypesFormats = Serialization.formats(NoTypeHints)

  implicit val stringToReportInfo : TypeConverter[String, ReportInfo] = new TypeConverter[String, ReportInfo] {
    def apply(v1: String): Option[ReportInfo] = {
      if (v1.length == 0) {
        Option.empty
      }
      Option.apply(read[ReportInfo](v1))
    }
  }
}
