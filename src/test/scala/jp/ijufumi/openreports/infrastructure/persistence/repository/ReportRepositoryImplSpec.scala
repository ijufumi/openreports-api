package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.Report
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class ReportRepositoryImplSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  "ReportRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new ReportRepositoryImpl()
  }

  // Note: The following tests require a test database connection
  // They should be implemented as integration tests with a test database
  // For proper testing, consider:
  // 1. Using an in-memory H2 database for testing
  // 2. Using testcontainers with PostgreSQL
  // 3. Creating a separate test configuration with test database

  /*
  "gets" should "return paginated reports for workspace" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()
    val workspaceId = "workspace-id"

    val report1 = Report(
      id = "report-1",
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Report 1",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM table1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val report2 = Report(
      id = "report-2",
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Report 2",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM table2",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report1)
    repository.register(db, report2)

    val (reports, count) = repository.gets(db, workspaceId, offset = 0, limit = 10)

    reports should not be empty
    count should be >= 2
  }

  it should "filter by template ID when provided" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()
    val workspaceId = "workspace-id"
    val templateId = "specific-template-id"

    val report = Report(
      id = "report-1",
      workspaceId = workspaceId,
      reportTemplateId = templateId,
      name = "Report 1",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM table1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report)

    val (reports, count) = repository.gets(db, workspaceId, templateId = templateId)

    reports should not be empty
    reports.forall(_.reportTemplateId == templateId) should be(true)
  }

  it should "respect pagination parameters" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()
    val workspaceId = "workspace-id"

    // Create multiple reports
    (1 to 20).foreach { i =>
      val report = Report(
        id = s"report-$i",
        workspaceId = workspaceId,
        reportTemplateId = "template-1",
        name = s"Report $i",
        dataSourceId = "datasource-1",
        query = s"SELECT * FROM table$i",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )
      repository.register(db, report)
    }

    val (reports, count) = repository.gets(db, workspaceId, offset = 5, limit = 5)

    reports.length should be <= 5
    count should be >= 20
  }

  "getsWithTemplate" should "return reports with template information" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()
    val workspaceId = "workspace-id"

    val report = Report(
      id = "report-1",
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Report 1",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM table1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report)

    val (reports, count) = repository.getsWithTemplate(db, workspaceId)

    reports should not be empty
  }

  "getById" should "return report when exists" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()
    val workspaceId = "workspace-id"
    val reportId = "report-id"

    val report = Report(
      id = reportId,
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Test Report",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM table1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report)

    val result = repository.getById(db, workspaceId, reportId)

    result should be(defined)
    result.get.id should equal(reportId)
    result.get.name should equal("Test Report")
  }

  it should "return None when report doesn't exist" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()

    val result = repository.getById(db, "workspace-id", "non-existent-id")

    result should be(None)
  }

  "register" should "create new report and return it" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()

    val report = Report(
      id = "new-report-id",
      workspaceId = "workspace-id",
      reportTemplateId = "template-1",
      name = "New Report",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM new_table",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val result = repository.register(db, report)

    result should be(defined)
    result.get.id should equal(report.id)
    result.get.name should equal(report.name)
  }

  "update" should "update existing report" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()

    val report = Report(
      id = "update-report-id",
      workspaceId = "workspace-id",
      reportTemplateId = "template-1",
      name = "Old Name",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM old_table",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report)

    val updatedReport = report.copy(
      name = "Updated Name",
      query = "SELECT * FROM updated_table"
    )

    repository.update(db, updatedReport)

    val result = repository.getById(db, report.workspaceId, report.id)
    result should be(defined)
    result.get.name should equal("Updated Name")
    result.get.query should equal("SELECT * FROM updated_table")
  }

  "delete" should "remove report from database" in {
    val db = mock[Database]
    val repository = new ReportRepositoryImpl()
    val workspaceId = "workspace-id"
    val reportId = "delete-report-id"

    val report = Report(
      id = reportId,
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Delete Report",
      dataSourceId = "datasource-1",
      query = "SELECT * FROM table1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report)
    repository.delete(db, workspaceId, reportId)

    val result = repository.getById(db, workspaceId, reportId)
    result should be(None)
  }
  */
}
