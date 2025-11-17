package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.Report
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.H2Profile.api._

class ReportRepositoryImplSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  var db: Database = _
  val repository = new ReportRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("report_test")
    H2DatabaseHelper.createSchema(db, reportQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, reportQuery)
  }

  "ReportRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new ReportRepositoryImpl()
  }

  "gets" should "return reports for workspace" in {
    val workspaceId = IDs.ulid()

    val report1 = Report(
      id = IDs.ulid(),
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Report 1",
      dataSourceId = Some("datasource-1"),
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val report2 = Report(
      id = IDs.ulid(),
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Report 2",
      dataSourceId = Some("datasource-1"),
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report1)
    repository.register(db, report2)

    val (reports, count) = repository.gets(db, workspaceId, offset = 0, limit = 10)

    reports should not be empty
    count should be(2)
  }

  it should "filter by template ID when provided" in {
    val workspaceId = IDs.ulid()
    val templateId = "specific-template-id"

    val report1 = Report(
      id = IDs.ulid(),
      workspaceId = workspaceId,
      reportTemplateId = templateId,
      name = "Report 1",
      dataSourceId = Some("datasource-1"),
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val report2 = Report(
      id = IDs.ulid(),
      workspaceId = workspaceId,
      reportTemplateId = "other-template",
      name = "Report 2",
      dataSourceId = Some("datasource-1"),
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report1)
    repository.register(db, report2)

    val (reports, count) = repository.gets(db, workspaceId, templateId = templateId)

    reports should not be empty
    reports.length should be(1)
    reports.forall(_.reportTemplateId == templateId) should be(true)
  }

  it should "respect pagination parameters" in {
    val workspaceId = IDs.ulid()

    // Create 10 reports
    (1 to 10).foreach { i =>
      val report = Report(
        id = IDs.ulid(),
        workspaceId = workspaceId,
        reportTemplateId = "template-1",
        name = s"Report $i",
        dataSourceId = Some("datasource-1"),
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )
      repository.register(db, report)
    }

    val (reports, count) = repository.gets(db, workspaceId, offset = 3, limit = 5)

    reports.length should be(5)
    count should be(10)
  }

  "getById" should "return report when exists" in {
    val workspaceId = IDs.ulid()
    val reportId = IDs.ulid()

    val report = Report(
      id = reportId,
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Test Report",
      dataSourceId = Some("datasource-1"),
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
    val result = repository.getById(db, "workspace-id", "non-existent-id")

    result should be(None)
  }

  "register" should "create new report and return it" in {
    val report = Report(
      id = IDs.ulid(),
      workspaceId = IDs.ulid(),
      reportTemplateId = "template-1",
      name = "New Report",
      dataSourceId = Some("datasource-1"),
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val result = repository.register(db, report)

    result should be(defined)
    result.get.id should equal(report.id)
    result.get.name should equal(report.name)
  }

  "update" should "update existing report" in {
    val report = Report(
      id = IDs.ulid(),
      workspaceId = IDs.ulid(),
      reportTemplateId = "template-1",
      name = "Old Name",
      dataSourceId = Some("datasource-1"),
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report)

    val updatedReport = report.copy(
      name = "Updated Name"
    )

    repository.update(db, updatedReport)

    val result = repository.getById(db, report.workspaceId, report.id)
    result should be(defined)
    result.get.name should equal("Updated Name")
  }

  "delete" should "remove report from database" in {
    val workspaceId = IDs.ulid()
    val reportId = IDs.ulid()

    val report = Report(
      id = reportId,
      workspaceId = workspaceId,
      reportTemplateId = "template-1",
      name = "Delete Report",
      dataSourceId = Some("datasource-1"),
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, report)
    repository.delete(db, workspaceId, reportId)

    val result = repository.getById(db, workspaceId, reportId)
    result should be(None)
  }
}
