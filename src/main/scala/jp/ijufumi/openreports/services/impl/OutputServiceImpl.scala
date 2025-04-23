package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.services.{DataSourceService, OutputService, StorageService}
import jp.ijufumi.openreports.utils.{Dates, Logging}
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes

import scala.util.Using
import org.jxls.common.Context
import org.jxls.jdbc.JdbcHelper
import org.jxls.util.JxlsHelper

import java.io.File
import java.nio.file.{Files, FileSystems, Path}
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
    val outputFile = FileSystems.getDefault.getPath(
      Config.OUTPUT_FILE_PATH,
      workspaceId,
      timeStamp,
      s"${inputFileName.substring(0, dotIndex)}_$timeStamp$suffix",
    )
    val outputPDFFile = FileSystems.getDefault.getPath(
      Config.OUTPUT_FILE_PATH,
      workspaceId,
      timeStamp,
      s"${inputFileName.substring(0, dotIndex)}_$timeStamp.pdf",
    )

    val outputDirectory = outputFile.getParent
    if (!Files.isDirectory(outputDirectory)) {
      Files.createDirectories(outputDirectory)
    }

    val context: Context = new Context()
    context.putVar("today", Dates.todayString())
    if (dataSourceId.isEmpty) {
      this.output(workspaceId, filePath, storageType, outputFile, context)
    } else {
      this.outputWithDataSource(
        workspaceId,
        filePath,
        storageType,
        dataSourceId.get,
        outputFile,
        context,
      )
    }

    if (asPDF) {
      this.convertToPDF(outputFile, outputPDFFile)
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
      context: Context,
  ): Unit = {
    Using(dataSourceService.connection(workspaceId, dataSourceId)) { conn =>
      val jdbcHelper = new JdbcHelper(conn)
      context.putVar("conn", conn)
      context.putVar("jdbc", jdbcHelper)
      this.output(workspaceId, filePath, storageType, outputFile, context)
    }
  }

  private def output(
      workspaceId: String,
      filePath: String,
      storageType: StorageTypes.StorageType,
      outputFile: Path,
      context: Context,
  ): Unit = {
    val inputFile = storageService.get(workspaceId, filePath, storageType)
    Using(Files.newInputStream(inputFile)) { inputs =>
      Using(Files.newOutputStream(outputFile)) { outputs =>
        JxlsHelper.getInstance().processTemplate(inputs, outputs, context)
      }
    }
  }

  private def convertToPDF(src: Path, dst: Path): Unit = {
    val res =
      os.call(cmd = ("soffice", "--headless", "--convert-to", "pdf:" + dst.toString, src.toString))
  }
}
