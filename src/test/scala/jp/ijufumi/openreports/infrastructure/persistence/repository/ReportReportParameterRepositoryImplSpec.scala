package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.ReportReportParameter
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  ReportReportParameter => ReportReportParameterEntity,
}
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class ReportReportParameterRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new ReportReportParameterRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("report_report_parameter_test")
    H2DatabaseHelper.createSchema(db, reportReportParameterQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, reportReportParameterQuery)
  }

  // Helper method to insert test report-parameter mapping directly into DB
  def insertReportReportParameter(
      id: String = IDs.ulid(),
      reportId: String = IDs.ulid(),
      reportParameterId: String = IDs.ulid(),
  ): ReportReportParameterEntity = {
    val entity = ReportReportParameterEntity(
      id,
      reportId,
      reportParameterId,
      System.currentTimeMillis(),
      System.currentTimeMillis(),
      1,
    )
    Await.result(db.run(reportReportParameterQuery += entity), 10.seconds)
    entity
  }

  "ReportReportParameterRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new ReportReportParameterRepositoryImpl()
  }

  "gets" should "return all report-parameter mappings without pagination" in {
    insertReportReportParameter()
    insertReportReportParameter()
    insertReportReportParameter()

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
    (1 to 15).foreach(_ => insertReportReportParameter())

    val (result, count) = repository.gets(db, offset = 5, limit = 5)

    result should have size 5
    count should equal(15)
  }

  it should "handle limit without offset" in {
    (1 to 10).foreach(_ => insertReportReportParameter())

    val (result, count) = repository.gets(db, limit = 3)

    result should have size 3
    count should equal(10)
  }

  it should "return all rows when offset=0 and limit=-1" in {
    (1 to 10).foreach(_ => insertReportReportParameter())

    val (result, count) = repository.gets(db, offset = 0, limit = -1)

    result should have size 10
    count should equal(10)
  }

  it should "apply offset even when limit is -1" in {
    (1 to 10).foreach(_ => insertReportReportParameter())

    val (result, count) = repository.gets(db, offset = 5, limit = -1)

    result should have size 5
    count should equal(10)
  }

  "getById" should "return mapping by ID" in {
    val targetId = IDs.ulid()
    val reportId = IDs.ulid()
    val parameterId = IDs.ulid()

    insertReportReportParameter(id = targetId, reportId = reportId, reportParameterId = parameterId)
    insertReportReportParameter()

    val result = repository.getById(db, targetId)

    result should be(defined)
    result.get.id should equal(targetId)
    result.get.reportId should equal(reportId)
    result.get.reportParameterId should equal(parameterId)
  }

  it should "return None when mapping doesn't exist" in {
    insertReportReportParameter()

    val result = repository.getById(db, "non-existent-id")

    result should be(None)
  }

  "getByIds" should "return mappings matching provided IDs" in {
    val id1 = IDs.ulid()
    val id2 = IDs.ulid()
    val id3 = IDs.ulid()

    insertReportReportParameter(id = id1)
    insertReportReportParameter(id = id2)
    insertReportReportParameter(id = id3)

    val result = repository.getByIds(db, Seq(id1, id3))

    result should have size 2
    result.map(_.id) should contain allOf (id1, id3)
    result.map(_.id) should not contain id2
  }

  it should "return empty sequence when no IDs match" in {
    insertReportReportParameter()

    val result = repository.getByIds(db, Seq("non-existent-id-1", "non-existent-id-2"))

    result should be(empty)
  }

  it should "return empty sequence when IDs list is empty" in {
    insertReportReportParameter()

    val result = repository.getByIds(db, Seq.empty)

    result should be(empty)
  }

  "register" should "insert new mapping and return it" in {
    val id = IDs.ulid()
    val reportId = IDs.ulid()
    val parameterId = IDs.ulid()

    val model = ReportReportParameter(
      id = id,
      reportId = reportId,
      reportParameterId = parameterId,
    )

    val result = repository.register(db, model)

    result should be(defined)
    result.get.id should equal(id)
    result.get.reportId should equal(reportId)
    result.get.reportParameterId should equal(parameterId)
  }

  it should "persist mapping to database" in {
    val id = IDs.ulid()
    val model = ReportReportParameter(
      id = id,
      reportId = IDs.ulid(),
      reportParameterId = IDs.ulid(),
    )

    repository.register(db, model)

    val retrieved = repository.getById(db, id)
    retrieved should be(defined)
  }

  "registerInBatch" should "insert multiple mappings" in {
    val models = (1 to 5).map { _ =>
      ReportReportParameter(
        id = IDs.ulid(),
        reportId = IDs.ulid(),
        reportParameterId = IDs.ulid(),
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
      ReportReportParameter(
        id = IDs.ulid(),
        reportId = IDs.ulid(),
        reportParameterId = IDs.ulid(),
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
    val originalParameterId = IDs.ulid()
    val newParameterId = IDs.ulid()

    insertReportReportParameter(
      id = id,
      reportId = originalReportId,
      reportParameterId = originalParameterId,
    )

    val updated = ReportReportParameter(
      id = id,
      reportId = originalReportId,
      reportParameterId = newParameterId,
    )

    repository.update(db, updated)

    val retrieved = repository.getById(db, id)
    retrieved should be(defined)
    retrieved.get.reportParameterId should equal(newParameterId)
    retrieved.get.reportId should equal(originalReportId)
  }

  it should "update timestamp on update" in {
    val id = IDs.ulid()
    val originalTimestamp = System.currentTimeMillis()

    insertReportReportParameter(id = id)
    Thread.sleep(10)

    val updated = ReportReportParameter(
      id = id,
      reportId = IDs.ulid(),
      reportParameterId = IDs.ulid(),
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
    insertReportReportParameter(id = targetId)
    insertReportReportParameter()

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

    insertReportReportParameter(reportId = targetReportId)
    insertReportReportParameter(reportId = targetReportId)
    insertReportReportParameter(reportId = otherReportId)

    repository.deleteByReportId(db, targetReportId)

    val (remaining, count) = repository.gets(db)
    count should equal(1)
    remaining.head.reportId should equal(otherReportId)
  }

  it should "not fail when report has no parameters" in {
    insertReportReportParameter()

    noException should be thrownBy repository.deleteByReportId(db, "non-existent-report-id")
  }

  "deleteByReportParameterId" should "remove all mappings for a parameter" in {
    val targetParameterId = IDs.ulid()
    val otherParameterId = IDs.ulid()

    insertReportReportParameter(reportParameterId = targetParameterId)
    insertReportReportParameter(reportParameterId = targetParameterId)
    insertReportReportParameter(reportParameterId = otherParameterId)

    repository.deleteByReportParameterId(db, targetParameterId)

    val (remaining, count) = repository.gets(db)
    count should equal(1)
    remaining.head.reportParameterId should equal(otherParameterId)
  }

  it should "not fail when parameter has no reports" in {
    insertReportReportParameter()

    noException should be thrownBy repository.deleteByReportParameterId(
      db,
      "non-existent-parameter-id",
    )
  }

  "ReportReportParameter conversion" should "correctly convert from entity to domain model" in {
    val id = IDs.ulid()
    val reportId = IDs.ulid()
    val parameterId = IDs.ulid()

    insertReportReportParameter(id = id, reportId = reportId, reportParameterId = parameterId)

    val result = repository.getById(db, id)

    result should be(defined)
    result.get.id should equal(id)
    result.get.reportId should equal(reportId)
    result.get.reportParameterId should equal(parameterId)
  }

  "ReportReportParameterRepository" should "support many-to-many relationship" in {
    val report1 = IDs.ulid()
    val report2 = IDs.ulid()
    val param1 = IDs.ulid()
    val param2 = IDs.ulid()

    // Report1 uses Param1 and Param2
    insertReportReportParameter(reportId = report1, reportParameterId = param1)
    insertReportReportParameter(reportId = report1, reportParameterId = param2)

    // Report2 also uses Param1
    insertReportReportParameter(reportId = report2, reportParameterId = param1)

    val (all, count) = repository.gets(db)
    count should equal(3)
  }

  it should "handle complex deletion scenarios" in {
    val report1 = IDs.ulid()
    val report2 = IDs.ulid()
    val param1 = IDs.ulid()
    val param2 = IDs.ulid()

    insertReportReportParameter(reportId = report1, reportParameterId = param1)
    insertReportReportParameter(reportId = report1, reportParameterId = param2)
    insertReportReportParameter(reportId = report2, reportParameterId = param1)
    insertReportReportParameter(reportId = report2, reportParameterId = param2)

    // Delete all mappings for param1
    repository.deleteByReportParameterId(db, param1)

    val (remaining, count) = repository.gets(db)
    count should equal(2)
    remaining.forall(_.reportParameterId == param2) should be(true)
  }

  it should "handle cascading deletes correctly" in {
    val reportId = IDs.ulid()
    val param1 = IDs.ulid()
    val param2 = IDs.ulid()
    val param3 = IDs.ulid()

    insertReportReportParameter(reportId = reportId, reportParameterId = param1)
    insertReportReportParameter(reportId = reportId, reportParameterId = param2)
    insertReportReportParameter(reportId = reportId, reportParameterId = param3)

    // Delete all parameters for a report
    repository.deleteByReportId(db, reportId)

    val (remaining, count) = repository.gets(db)
    count should equal(0)
  }
}
