package jp.ijufumi.openreports.controller.settings

import java.nio.file.{FileSystems, Files}

import jp.ijufumi.openreports.controller.common.{Controllers, I18nFeature}
import skinny.controller.feature.FileUploadFeature
import skinny.controller.{Params, SkinnyServlet}
import skinny.validator.{paramKey, required}

class ReportSettingsController extends SkinnyServlet
  with FileUploadFeature
  with I18nFeature {

  val path = rootPath + "/report"

  val requestParams = Params(params)
  val validateParams = validation(
    requestParams,
    paramKey("name") is required,
    paramKey("file") is required
  )

  def uploadForm = render(rootPath + "/report/fileUpload")

  def doUploadFile = {
    if (validateParams.validate) {
      logger.info("uploadFile is %s".format(params.get("name").get))
      var file = fileParams("file")
      var tempFile = FileSystems.getDefault.getPath("/tmp/", file.getName)
      Files.write(tempFile, file.get())
      redirect(url(Controllers.reportSettings.uploadFormUrl))
    } else {
      logger.info("[REQUST] name/file is empty.")
      render(url(Controllers.reportSettings.uploadFormUrl))
    }
  }
}
