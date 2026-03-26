package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.ReportGroupReport
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  ReportGroupReport => ReportGroupReportEntity,
}
import jp.ijufumi.openreports.utils.{Dates, IDs}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class ReportGroupReportRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new ReportGroupReportRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("report_group_report_test")
    H2DatabaseHelper.createSchema(db, reportGroupReportQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, reportGroupReportQuery)
  }

  // Helper method to insert test report-group-report mapping directly into DB
  def insertReportGroupReport(
      id: String = IDs.ulid(),
      reportId: String = IDs.ulid(),
      reportGroupId: String = IDs.ulid(),
  ): ReportGroupReportEntity = {
    val entity = ReportGroupReportEntity(
      id,
      reportId,
      reportGroupId,
      Dates.currentTimestamp(),
      Dates.currentTimestamp(),
      1,
    )
    Await.result(db.run(reportGroupReportQuery += entity), 10.seconds)
    entity
  }

  "ReportGroupReportRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new ReportGroupReportRepositoryImpl()
  }

  "gets" should "return all report-group mappings without pagination" in {
    insertReportGroupReport()
    insertReportGroupReport()
    insertReportGroupReport()

    val (result, count) = repository.gets(db)

    result should have size 3
    count should equal(3)
  }

  it should "return empty sequence when no mappings exist" in {
    val (result, count) = repository.gets(db)

    result should be(empty)
    count should equal(0)
  }

  it should "return paginated results with offset and limit" in {
    (1 to 15).foreach(_ => insertReportGroupReport())

    val (result, count) = repository.gets(db, offset = 5, limit = 5)

    result should have size 5
    count should equal(15)
  }

  it should "handle limit without offset" in {
    (1 to 10).foreach(_ => insertReportGroupReport())

    val (result, count) = repository.gets(db, limit = 3)

    result should have size 3
    count should equal(10)
  }

  it should "ignore pagination when limit is -1" in {
    (1 to 10).foreach(_ => insertReportGroupReport())

    val (result, count) = repository.gets(db, offset = 5, limit = -1)

    result should have size 10
    count should equal(10)
  }

  "getById" should "return mapping by ID" in {
    val targetId = IDs.ulid()
    val reportId = IDs.ulid()
    val reportGroupId = IDs.ulid()

    insertReportGroupReport(id = targetId, reportId = reportId, reportGroupId = reportGroupId)
    insertReportGroupReport()

    val result = repository.getById(db, targetId)

    result should be(defined)
    result.get.id should equal(targetId)
    result.get.reportId should equal(reportId)
    result.get.reportGroupId should equal(reportGroupId)
  }

  it should "return None when mapping doesn't exist" in {
    insertReportGroupReport()

    val result = repository.getById(db, "non-existent-id")

    result should be(None)
  }

  "getByIds" should "return mappings matching provided IDs" in {
    val id1 = IDs.ulid()
    val id2 = IDs.ulid()
    val id3 = IDs.ulid()

    insertReportGroupReport(id = id1)
    insertReportGroupReport(id = id2)
    insertReportGroupReport(id = id3)

    val result = repository.getByIds(db, Seq(id1, id3))

    result should have size 2
    result.map(_.id) should contain allOf (id1, id3)
    result.map(_.id) should not contain id2
  }

  it should "return empty sequence when no IDs match" in {
    insertReportGroupReport()

    val result = repository.getByIds(db, Seq("non-existent-id-1", "non-existent-id-2"))

    result should be(empty)
  }

  it should "return empty sequence when IDs list is empty" in {
    insertReportGroupReport()

    val result = repository.getByIds(db, Seq.empty)

    result should be(empty)
  }

  "register" should "insert new mapping and return it" in {
    val id = IDs.ulid()
    val reportId = IDs.ulid()
    val reportGroupId = IDs.ulid()

    val model = ReportGroupReport(
      id = id,
      reportId = reportId,
      reportGroupId = reportGroupId,
    )

    val result = repository.register(db, model)

    result should be(defined)
    result.get.id should equal(id)
    result.get.reportId should equal(reportId)
    result.get.reportGroupId should equal(reportGroupId)
  }

  it should "persist mapping to database" in {
    val id = IDs.ulid()
    val model = ReportGroupReport(
      id = id,
      reportId = IDs.ulid(),
      reportGroupId = IDs.ulid(),
    )

    repository.register(db, model)

    val retrieved = repository.getById(db, id)
    retrieved should be(defined)
  }

  "registerInBatch" should "insert multiple mappings" in {
    val models = (1 to 5).map { _ =>
      ReportGroupReport(
        id = IDs.ulid(),
        reportId = IDs.ulid(),
        reportGroupId = IDs.ulid(),
      )
    }

    val result = repository.registerInBatch(db, models)

    result should have size 5
    val sortedResult = result.sortBy(_.id)
    val sortedModels = models.sortBy(_.id)
    sortedResult.map(_.id) should equal(sortedModels.map(_.id))
  }

  it should "persist all mappings to database" in {
    val models = (1 to 3).map { _ =>
      ReportGroupReport(
        id = IDs.ulid(),
        reportId = IDs.ulid(),
        reportGroupId = IDs.ulid(),
      )
    }

    repository.registerInBatch(db, models)

    val (all, count) = repository.gets(db)
    all should have size 3
    count should equal(3)
  }

  "update" should "update existing mapping" in {
    val id = IDs.ulid()
    val originalReportId = IDs.ulid()
    val originalGroupId = IDs.ulid()
    val newReportId = IDs.ulid()

    insertReportGroupReport(id = id, reportId = originalReportId, reportGroupId = originalGroupId)

    val updated = ReportGroupReport(
      id = id,
      reportId = newReportId,
      reportGroupId = originalGroupId,
    )

    repository.update(db, updated)

    val retrieved = repository.getById(db, id)
    retrieved should be(defined)
    retrieved.get.reportId should equal(newReportId)
    retrieved.get.reportGroupId should equal(originalGroupId)
  }

  it should "update timestamp on update" in {
    val id = IDs.ulid()
    val originalTimestamp = System.currentTimeMillis()

    insertReportGroupReport(id = id)
    Thread.sleep(10)

    val updated = ReportGroupReport(
      id = id,
      reportId = IDs.ulid(),
      reportGroupId = IDs.ulid(),
      createdAt = originalTimestamp,
      updatedAt = originalTimestamp,
    )

    repository.update(db, updated)

    val retrieved = repository.getById(db, id)
    retrieved should be(defined)
    retrieved.get.updatedAt should be > originalTimestamp
  }

  "delete" should "remove mapping by ID" in {
    val targetId = IDs.ulid()
    insertReportGroupReport(id = targetId)
    insertReportGroupReport()

    repository.delete(db, targetId)

    val result = repository.getById(db, targetId)
    result should be(None)

    val (all, count) = repository.gets(db)
    count should equal(1)
  }

  it should "not fail when deleting non-existent mapping" in {
    noException should be thrownBy repository.delete(db, "non-existent-id")
  }

  "deleteByReportId" should "remove all mappings for a report" in {
    val targetReportId = IDs.ulid()
    val otherReportId = IDs.ulid()

    insertReportGroupReport(reportId = targetReportId)
    insertReportGroupReport(reportId = targetReportId)
    insertReportGroupReport(reportId = otherReportId)

    repository.deleteByReportId(db, targetReportId)

    val (remaining, count) = repository.gets(db)
    count should equal(1)
    remaining.head.reportId should equal(otherReportId)
  }

  it should "not fail when report has no mappings" in {
    insertReportGroupReport()

    noException should be thrownBy repository.deleteByReportId(db, "non-existent-report-id")
  }

  "deleteByReportGroupId" should "remove all mappings for a report group" in {
    val targetGroupId = IDs.ulid()
    val otherGroupId = IDs.ulid()

    insertReportGroupReport(reportGroupId = targetGroupId)
    insertReportGroupReport(reportGroupId = targetGroupId)
    insertReportGroupReport(reportGroupId = otherGroupId)

    repository.deleteByReportGroupId(db, targetGroupId)

    val (remaining, count) = repository.gets(db)
    count should equal(1)
    remaining.head.reportGroupId should equal(otherGroupId)
  }

  it should "not fail when report group has no mappings" in {
    insertReportGroupReport()

    noException should be thrownBy repository.deleteByReportGroupId(db, "non-existent-group-id")
  }

  "ReportGroupReport conversion" should "correctly convert from entity to domain model" in {
    val id = IDs.ulid()
    val reportId = IDs.ulid()
    val reportGroupId = IDs.ulid()

    insertReportGroupReport(id = id, reportId = reportId, reportGroupId = reportGroupId)

    val result = repository.getById(db, id)

    result should be(defined)
    result.get.id should equal(id)
    result.get.reportId should equal(reportId)
    result.get.reportGroupId should equal(reportGroupId)
  }

  "ReportGroupReportRepository" should "support many-to-many relationship" in {
    val report1 = IDs.ulid()
    val report2 = IDs.ulid()
    val group1 = IDs.ulid()
    val group2 = IDs.ulid()

    // Report1 belongs to Group1 and Group2
    insertReportGroupReport(reportId = report1, reportGroupId = group1)
    insertReportGroupReport(reportId = report1, reportGroupId = group2)

    // Report2 also belongs to Group1
    insertReportGroupReport(reportId = report2, reportGroupId = group1)

    val (all, count) = repository.gets(db)
    count should equal(3)
  }

  it should "handle complex deletion scenarios" in {
    val report1 = IDs.ulid()
    val report2 = IDs.ulid()
    val group1 = IDs.ulid()
    val group2 = IDs.ulid()

    insertReportGroupReport(reportId = report1, reportGroupId = group1)
    insertReportGroupReport(reportId = report1, reportGroupId = group2)
    insertReportGroupReport(reportId = report2, reportGroupId = group1)
    insertReportGroupReport(reportId = report2, reportGroupId = group2)

    // Delete all mappings for report1
    repository.deleteByReportId(db, report1)

    val (remaining, count) = repository.gets(db)
    count should equal(2)
    remaining.forall(_.reportId == report2) should be(true)
  }
}
