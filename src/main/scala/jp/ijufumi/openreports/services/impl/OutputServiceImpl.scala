package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.entities.enums.StorageTypes.StorageType
import jp.ijufumi.openreports.services.{DataSourceService, OutputService, StorageService}
import jp.ijufumi.openreports.utils.{Dates, Logging}

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
      storageType: StorageType,
      dataSourceId: String,
  ): Option[File] = {
    val inputFileName = new File(filePath).getName
    val dotIndex = inputFileName.lastIndexOf('.')
    val suffix = if (dotIndex != -1) inputFileName.substring(dotIndex) else ""
    val timeStamp = Dates.format(LocalDateTime.now())
    val outputFile = FileSystems.getDefault.getPath(
      Config.OUTPUT_FILE_PATH,
      s"/tmp/${inputFileName.substring(0, dotIndex)}_${timeStamp}${suffix}",
    )

    val outputDirectory = outputFile.getParent
    if (!Files.isDirectory(outputDirectory)) {
      Files.createDirectories(outputDirectory)
    }

    if (dataSourceId == null) {
      this.output(workspaceId, filePath, storageType, outputFile)
    } else {
      this.outputWithDataSource(workspaceId, filePath, storageType, dataSourceId, outputFile)
    }

    Some(outputFile.toFile)
  }

  private def outputWithDataSource(
      workspaceId: String,
      filePath: String,
      storageType: StorageType,
      dataSourceId: String,
      outputFile: Path,
  ): Unit = {
    Using(dataSourceService.connection(dataSourceId)) { conn =>
      val jdbcHelper = new JdbcHelper(conn)
      val context = new Context()
      context.putVar("conn", conn)
      context.putVar("jdbc", jdbcHelper)
      this.output(workspaceId, filePath, storageType, outputFile, context)
    }
  }

  private def output(
      workspaceId: String,
      filePath: String,
      storageType: StorageType,
      outputFile: Path,
      context: Context = new Context(),
  ): Unit = {
    Using(storageService.get(workspaceId, filePath, storageType)) { inputs =>
      Using(Files.newOutputStream(outputFile)) { outputs =>
        JxlsHelper.getInstance().processTemplate(inputs, outputs, context)
      }
    }
  }
}
