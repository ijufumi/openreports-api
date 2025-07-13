package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.services.{DataSourceService, OutputService, StorageService}
import jp.ijufumi.openreports.utils.{Dates, Logging}
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import org.jxls.builder.JxlsOutputFile

import scala.util.Using
import org.jxls.jdbc.DatabaseAccess
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder

import java.io.File
import java.nio.file.{FileSystems, Files, Path}
import java.time.LocalDateTime

class OutputServiceImpl @Inject() (
    dataSourceService: DataSourceService,
    storageService: StorageService,
) extends OutputService
    with Logging {
  override def output(
      workspaceId: String,
      filePath: String,
      storageType: StorageTypes.StorageType,
      dataSourceId: Option[String],
      asPDF: Boolean,
  ): Option[File] = {
    val inputFileName = new File(filePath).getName
    val dotIndex = inputFileName.lastIndexOf('.')
    val suffix = if (dotIndex != -1) inputFileName.substring(dotIndex) else ""
    val timeStamp = Dates.format(LocalDateTime.now())
    val outputDir = FileSystems.getDefault.getPath(
      Config.OUTPUT_FILE_PATH,
      workspaceId,
      timeStamp,
    )
    val outputFile = FileSystems.getDefault.getPath(
      outputDir.toString,
      s"${inputFileName.substring(0, dotIndex)}_$timeStamp$suffix",
    )
    val outputPDFFile = FileSystems.getDefault.getPath(
      outputDir.toString,
      s"${inputFileName.substring(0, dotIndex)}_$timeStamp.pdf",
    )

    val outputDirectory = outputFile.getParent
    if (!Files.isDirectory(outputDirectory)) {
      Files.createDirectories(outputDirectory)
    }

    val dataMap = new java.util.HashMap[String, Object]();
    dataMap.put("today", Dates.todayString())
    if (dataSourceId.isEmpty) {
      this.output(workspaceId, filePath, storageType, outputFile, dataMap)
    } else {
      this.outputWithDataSource(
        workspaceId,
        filePath,
        storageType,
        dataSourceId.get,
        outputFile,
        dataMap,
      )
    }

    if (asPDF) {
      this.convertToPDF(outputFile, outputDir)
      return Some(outputPDFFile.toFile)
    }
    Some(outputFile.toFile)
  }

  private def outputWithDataSource(
      workspaceId: String,
      filePath: String,
      storageType: StorageTypes.StorageType,
      dataSourceId: String,
      outputFile: Path,
      dataMap: java.util.HashMap[String, Object],
  ): Unit = {
    val result = Using(dataSourceService.connection(workspaceId, dataSourceId)) { conn =>
      val jdbcHelper = new DatabaseAccess(conn)
      dataMap.put("conn", conn)
      dataMap.put("jdbc", jdbcHelper)
      this.output(workspaceId, filePath, storageType, outputFile, dataMap)
    }
    if (result.isFailure) {
      logger.error(s"datasource error", result.failed.get)
      throw result.failed.get
    }
  }

  private def output(
      workspaceId: String,
      filePath: String,
      storageType: StorageTypes.StorageType,
      outputFile: Path,
      dataMap: java.util.HashMap[String, Object],
  ): Unit = {
    val inputFile = storageService.get(workspaceId, filePath, storageType)
    val inputResult = Using(Files.newInputStream(inputFile)) { inputs =>
      JxlsPoiTemplateFillerBuilder.newInstance().withTemplate(inputs).build().fill(dataMap, new JxlsOutputFile(outputFile.toFile))
    }
    if (inputResult.isFailure) {
      logger.error(s"output error", inputResult.failed.get)
      throw inputResult.failed.get
    }
  }

  private def convertToPDF(src: Path, dstDir: Path): Unit = {
    val res = {
      os.call(cmd = ("soffice", "--headless", "--convert-to", "pdf:writer_pdf_Export", "--outdir", dstDir.toString, src.toString))
    }
    logger.info(s"soffice command: ${res.command.mkString(" ")}")
    if (res.exitCode != 0) {
      logger.error(s"soffice error: ${res.err}")
    }
  }
}
