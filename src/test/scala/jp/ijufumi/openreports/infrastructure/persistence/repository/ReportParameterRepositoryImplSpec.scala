package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.ReportParameter
import jp.ijufumi.openreports.domain.models.value.enums.{EmbeddedFunctionTypes, ParameterTypes}
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database

class ReportParameterRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new ReportParameterRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("reportparameter_test")
    H2DatabaseHelper.createSchema(db, reportParameterQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, reportParameterQuery)
  }

  // Helper method to create a test report parameter
  def createTestReportParameter(
      workspaceId: String,
      parameterType: ParameterTypes.ParameterType = ParameterTypes.FixedValue,
      embeddedFunctionType: Option[EmbeddedFunctionTypes.EmbeddedFunctionType] = None,
      value: Option[String] = Some("test-value"),
  ): ReportParameter = {
    ReportParameter(
      id = IDs.ulid(),
      workspaceId = workspaceId,
      parameterType = parameterType,
      embeddedFunctionType = embeddedFunctionType,
      value = value,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )
  }

  "ReportParameterRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new ReportParameterRepositoryImpl()
  }

  "register" should "create new report parameter and return it" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(workspaceId)

    val result = repository.register(db, parameter)

    result should be(defined)
    result.get.id should equal(parameter.id)
    result.get.workspaceId should equal(workspaceId)
    result.get.parameterType should equal(parameter.parameterType)
  }

  it should "persist parameter with fixed value" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(
      workspaceId,
      parameterType = ParameterTypes.FixedValue,
      value = Some("fixed-test-value"),
    )

    repository.register(db, parameter)

    val retrieved = repository.getById(db, workspaceId, parameter.id)
    retrieved should be(defined)
    retrieved.get.parameterType should equal(ParameterTypes.FixedValue)
    retrieved.get.value should equal(Some("fixed-test-value"))
  }

  it should "persist parameter with embedded function" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(
      workspaceId,
      parameterType = ParameterTypes.EmbeddedFunction,
      embeddedFunctionType = Some(EmbeddedFunctionTypes.Today),
    )

    repository.register(db, parameter)

    val retrieved = repository.getById(db, workspaceId, parameter.id)
    retrieved should be(defined)
    retrieved.get.parameterType should equal(ParameterTypes.EmbeddedFunction)
    retrieved.get.embeddedFunctionType should equal(Some(EmbeddedFunctionTypes.Today))
  }

  "getById" should "return parameter when exists" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(workspaceId)
    repository.register(db, parameter)

    val result = repository.getById(db, workspaceId, parameter.id)

    result should be(defined)
    result.get.id should equal(parameter.id)
    result.get.workspaceId should equal(workspaceId)
  }

  it should "return None when parameter doesn't exist" in {
    val result = repository.getById(db, "workspace-id", "param-id")

    result should be(None)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val param1 = createTestReportParameter(workspaceId1)
    val param2 = createTestReportParameter(workspaceId2)

    repository.register(db, param1)
    repository.register(db, param2)

    val result = repository.getById(db, workspaceId1, param1.id)

    result should be(defined)
    result.get.workspaceId should equal(workspaceId1)
  }

  "gets" should "return parameters for workspace with count" in {
    val workspaceId = IDs.ulid()

    val param1 = createTestReportParameter(workspaceId, value = Some("value1"))
    val param2 = createTestReportParameter(workspaceId, value = Some("value2"))

    repository.register(db, param1)
    repository.register(db, param2)

    val (params, count) = repository.gets(db, workspaceId, offset = 0, limit = 10)

    params should have size 2
    count should equal(2)
  }

  it should "respect pagination offset and limit" in {
    val workspaceId = IDs.ulid()

    // Create 10 parameters
    (1 to 10).foreach { i =>
      val param = createTestReportParameter(workspaceId, value = Some(s"value-$i"))
      repository.register(db, param)
    }

    val (params, count) = repository.gets(db, workspaceId, offset = 3, limit = 5)

    params should have size 5
    count should equal(10)
  }

  it should "return all parameters when limit is -1" in {
    val workspaceId = IDs.ulid()

    (1 to 15).foreach { i =>
      val param = createTestReportParameter(workspaceId)
      repository.register(db, param)
    }

    val (params, count) = repository.gets(db, workspaceId, offset = 0, limit = -1)

    params should have size 15
    count should equal(15)
  }

  it should "return empty sequence when no parameters exist" in {
    val (params, count) = repository.gets(db, IDs.ulid(), offset = 0, limit = 10)

    params should be(empty)
    count should equal(0)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    repository.register(db, createTestReportParameter(workspaceId1))
    repository.register(db, createTestReportParameter(workspaceId2))

    val (params, count) = repository.gets(db, workspaceId1, offset = 0, limit = 10)

    params should have size 1
    count should equal(1)
    params.head.workspaceId should equal(workspaceId1)
  }

  "update" should "update existing parameter" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(workspaceId, value = Some("old-value"))
    repository.register(db, parameter)

    Thread.sleep(10) // Ensure timestamp difference
    val updated = parameter.copy(value = Some("new-value"))
    repository.update(db, updated)

    val result = repository.getById(db, workspaceId, parameter.id)
    result should be(defined)
    result.get.value should equal(Some("new-value"))
    result.get.updatedAt should be > parameter.updatedAt
  }

  it should "update parameter type" in {
    val workspaceId = IDs.ulid()
    val parameter =
      createTestReportParameter(workspaceId, parameterType = ParameterTypes.FixedValue)
    repository.register(db, parameter)

    val updated = parameter.copy(
      parameterType = ParameterTypes.EmbeddedFunction,
      embeddedFunctionType = Some(EmbeddedFunctionTypes.Today),
    )
    repository.update(db, updated)

    val result = repository.getById(db, workspaceId, parameter.id)
    result should be(defined)
    result.get.parameterType should equal(ParameterTypes.EmbeddedFunction)
    result.get.embeddedFunctionType should equal(Some(EmbeddedFunctionTypes.Today))
  }

  "delete" should "remove parameter from database" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(workspaceId)
    repository.register(db, parameter)

    repository.delete(db, workspaceId, parameter.id)

    val result = repository.getById(db, workspaceId, parameter.id)
    result should be(None)
  }

  it should "only delete parameters for specified workspace" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val param1 = createTestReportParameter(workspaceId1)
    val param2 = createTestReportParameter(workspaceId2)

    repository.register(db, param1)
    repository.register(db, param2)

    repository.delete(db, workspaceId1, param1.id)

    val (params1, _) = repository.gets(db, workspaceId1, 0, 10)
    val (params2, _) = repository.gets(db, workspaceId2, 0, 10)

    params1 should be(empty)
    params2 should have size 1
  }

  "ReportParameterRepository" should "handle None value" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(workspaceId, value = None)

    val result = repository.register(db, parameter)

    result should be(defined)
    result.get.value should be(None)
  }

  it should "handle empty string value" in {
    val workspaceId = IDs.ulid()
    val parameter = createTestReportParameter(workspaceId, value = Some(""))

    val result = repository.register(db, parameter)

    result should be(defined)
    result.get.value should equal(Some(""))
  }

  it should "handle very long values" in {
    val workspaceId = IDs.ulid()
    val longValue = "A" * 500
    val parameter = createTestReportParameter(workspaceId, value = Some(longValue))

    val result = repository.register(db, parameter)

    result should be(defined)
    result.get.value should equal(Some(longValue))
  }

  it should "handle special characters in value" in {
    val workspaceId = IDs.ulid()
    val specialValue = "Value with 'quotes' and \"double quotes\" & special chars!@#$%"
    val parameter = createTestReportParameter(workspaceId, value = Some(specialValue))

    val result = repository.register(db, parameter)

    result should be(defined)
    result.get.value should equal(Some(specialValue))
  }

  it should "handle unicode characters in value" in {
    val workspaceId = IDs.ulid()
    val unicodeValue = "パラメータ値 日本語テスト"
    val parameter = createTestReportParameter(workspaceId, value = Some(unicodeValue))

    val result = repository.register(db, parameter)

    result should be(defined)
    result.get.value should equal(Some(unicodeValue))
  }

  it should "handle both parameter types correctly" in {
    val workspaceId = IDs.ulid()

    val fixedParam = createTestReportParameter(
      workspaceId,
      parameterType = ParameterTypes.FixedValue,
      value = Some("fixed"),
    )
    val embeddedParam = createTestReportParameter(
      workspaceId,
      parameterType = ParameterTypes.EmbeddedFunction,
      embeddedFunctionType = Some(EmbeddedFunctionTypes.Today),
    )

    repository.register(db, fixedParam)
    repository.register(db, embeddedParam)

    val (params, _) = repository.gets(db, workspaceId, 0, 10)

    params should have size 2
    params.map(
      _.parameterType,
    ) should contain allOf (ParameterTypes.FixedValue, ParameterTypes.EmbeddedFunction)
  }
}
