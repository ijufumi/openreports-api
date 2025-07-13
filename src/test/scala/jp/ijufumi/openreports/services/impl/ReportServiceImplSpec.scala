package jp.ijufumi.openreports.services.impl

import _root_.jp.ijufumi.openreports.domain.models.entity.{Report => ReportModel, ReportTemplate => ReportTemplateModel}
import _root_.jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import _root_.jp.ijufumi.openreports.infrastructure.datastores.database.repositories._
import _root_.jp.ijufumi.openreports.presentation.models.requests.CreateReport
import _root_.jp.ijufumi.openreports.presentation.models.responses.{Lists, Report, ReportTemplate}
import _root_.jp.ijufumi.openreports.services.{DataSourceService, OutputService, StorageService}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class ReportServiceImplSpec extends AnyFlatSpec with MockitoSugar {

  "getReports" should "return reports" in {
    // mock
    val db = mock[Database]
    val reportRepository = mock[ReportRepository]
    val templateRepository = mock[ReportTemplateRepository]
    val reportGroupRepository = mock[ReportGroupRepository]
    val reportGroupReportRepository = mock[ReportGroupReportRepository]
    val dataSourceService = mock[DataSourceService]
    val outputService = mock[OutputService]
    val storageService = mock[StorageService]

    val reportService = new ReportServiceImpl(
      db,
      reportRepository,
      templateRepository,
      reportGroupRepository,
      reportGroupReportRepository,
      dataSourceService,
      outputService,
      storageService,
    )

    val workspaceId = "1"
    val page = 1
    val limit = 10
    val offset = page * limit
    val entityTemplate = ReportTemplateModel("1", "test-template", "test.xlsx", workspaceId, StorageTypes.Local, 100L, 0L, 0L)
    val entityReport = ReportModel("1", "test-report", "1", None, workspaceId, 0L, 0L).copy(reportTemplate = Some(entityTemplate))

    val mockResult: (Seq[ReportModel], Int) = (Seq(entityReport), 1)

    when(reportRepository.getsWithTemplate(db, workspaceId, offset, limit, ""))
      .thenReturn(mockResult)

    // when
    val actual = reportService.getReports(workspaceId, page, limit)

    // then
    assert(actual.isInstanceOf[Lists[Report]])
    assert(actual.items.length == 1)
    assert(actual.count == 1)
    assert(actual.items.head.name == "test-report")
  }

  "createReport" should "create report" in {
    // mock
    val db = mock[Database]
    val reportRepository = mock[ReportRepository]
    val templateRepository = mock[ReportTemplateRepository]
    val reportGroupRepository = mock[ReportGroupRepository]
    val reportGroupReportRepository = mock[ReportGroupReportRepository]
    val dataSourceService = mock[DataSourceService]
    val outputService = mock[OutputService]
    val storageService = mock[StorageService]

    val reportService = new ReportServiceImpl(
      db,
      reportRepository,
      templateRepository,
      reportGroupRepository,
      reportGroupReportRepository,
      dataSourceService,
      outputService,
      storageService,
    )

    val workspaceId = "1"
    val request = CreateReport("test", "1", None)
    val entityReport =
      ReportModel("random-id", request.name, request.templateId, request.dataSourceId, workspaceId, 0L, 0L)

    val responseTemplate = ReportTemplate("1", "test-template", "test.xlsx", StorageTypes.Local, 100L, 0L, 0L)
    val responseReport = Report(
      entityReport.id,
      entityReport.name,
      entityReport.templateId,
      entityReport.dataSourceId,
      0L,
      0L,
      Some(responseTemplate),
    )

    when(reportRepository.register(any(classOf[Database]), any(classOf[ReportModel])))
      .thenReturn(Some(entityReport))
    when(reportRepository.getByIdWithTemplate(any(classOf[Database]), any(classOf[String]), any[String]()))
      .thenReturn(Some(entityReport.copy(reportTemplate = Some(ReportTemplateModel("1", "test-template", "test.xlsx", workspaceId, StorageTypes.Local, 100L, 0L, 0L)))))

    // when
    val actual = reportService.createReport(workspaceId, request)

    // then
    assert(actual.isDefined)
    assert(actual.get.name == request.name)
  }
}
