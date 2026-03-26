package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.ReportTemplate
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database

class ReportTemplateRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new ReportTemplateRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("template_test")
    H2DatabaseHelper.createSchema(db, templateQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, templateQuery)
  }

  // Helper method to create a test template
  def createTestTemplate(
      workspaceId: String,
      name: String = "Test Template",
      storageType: StorageTypes.StorageType = StorageTypes.Local,
  ): ReportTemplate = {
    ReportTemplate(
      id = IDs.ulid(),
      name = name,
      filePath = "/path/to/template.xlsx",
      workspaceId = workspaceId,
      storageType = storageType,
      fileSize = 1024L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )
  }

  "ReportTemplateRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new ReportTemplateRepositoryImpl()
  }

  "register" should "create new template and return it" in {
    val workspaceId = IDs.ulid()
    val template = createTestTemplate(workspaceId)

    val result = repository.register(db, template)

    result should be(defined)
    result.get.id should equal(template.id)
    result.get.name should equal(template.name)
    result.get.filePath should equal(template.filePath)
  }

  it should "persist template to database" in {
    val workspaceId = IDs.ulid()
    val template = createTestTemplate(workspaceId)

    repository.register(db, template)

    val retrieved = repository.getById(db, workspaceId, template.id)
    retrieved should be(defined)
    retrieved.get.name should equal(template.name)
  }

  "getById" should "return template when exists" in {
    val workspaceId = IDs.ulid()
    val template = createTestTemplate(workspaceId)
    repository.register(db, template)

    val result = repository.getById(db, workspaceId, template.id)

    result should be(defined)
    result.get.id should equal(template.id)
    result.get.workspaceId should equal(workspaceId)
  }

  it should "return None when template doesn't exist" in {
    val workspaceId = IDs.ulid()

    val result = repository.getById(db, workspaceId, "non-existent-id")

    result should be(None)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val template1 = createTestTemplate(workspaceId1)
    val template2 = createTestTemplate(workspaceId2)

    repository.register(db, template1)
    repository.register(db, template2)

    val result = repository.getById(db, workspaceId1, template1.id)

    result should be(defined)
    result.get.workspaceId should equal(workspaceId1)
  }

  "gets" should "return templates for workspace with count" in {
    val workspaceId = IDs.ulid()

    val template1 = createTestTemplate(workspaceId, "Template 1")
    val template2 = createTestTemplate(workspaceId, "Template 2")

    repository.register(db, template1)
    repository.register(db, template2)

    val (templates, count) = repository.gets(db, workspaceId, offset = 0, limit = 10)

    templates should have size 2
    count should equal(2)
    templates.map(_.name) should contain allOf ("Template 1", "Template 2")
  }

  it should "respect pagination offset and limit" in {
    val workspaceId = IDs.ulid()

    // Create 10 templates
    (1 to 10).foreach { i =>
      val template = createTestTemplate(workspaceId, s"Template $i")
      repository.register(db, template)
    }

    val (templates, count) = repository.gets(db, workspaceId, offset = 3, limit = 5)

    templates should have size 5
    count should equal(10)
  }

  it should "return all templates when limit is 0" in {
    val workspaceId = IDs.ulid()

    (1 to 15).foreach { i =>
      val template = createTestTemplate(workspaceId, s"Template $i")
      repository.register(db, template)
    }

    val (templates, count) = repository.gets(db, workspaceId, offset = 0, limit = 0)

    templates should have size 15
    count should equal(15)
  }

  it should "handle offset beyond results" in {
    val workspaceId = IDs.ulid()

    val template = createTestTemplate(workspaceId)
    repository.register(db, template)

    val (templates, count) = repository.gets(db, workspaceId, offset = 10, limit = 10)

    templates should be(empty)
    count should equal(1)
  }

  it should "return empty sequence when no templates exist" in {
    val workspaceId = IDs.ulid()

    val (templates, count) = repository.gets(db, workspaceId, offset = 0, limit = 10)

    templates should be(empty)
    count should equal(0)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val template1 = createTestTemplate(workspaceId1)
    val template2 = createTestTemplate(workspaceId2)

    repository.register(db, template1)
    repository.register(db, template2)

    val (templates, count) = repository.gets(db, workspaceId1, offset = 0, limit = 10)

    templates should have size 1
    count should equal(1)
    templates.head.workspaceId should equal(workspaceId1)
  }

  "update" should "update existing template" in {
    val workspaceId = IDs.ulid()
    val template = createTestTemplate(workspaceId)
    repository.register(db, template)

    val updatedTemplate = template.copy(
      name = "Updated Template Name",
      filePath = "/new/path/template.xlsx",
      fileSize = 2048L,
    )

    repository.update(db, updatedTemplate)

    val result = repository.getById(db, workspaceId, template.id)
    result should be(defined)
    result.get.name should equal("Updated Template Name")
    result.get.filePath should equal("/new/path/template.xlsx")
    result.get.fileSize should equal(2048L)
  }

  "delete" should "remove template from database" in {
    val workspaceId = IDs.ulid()
    val template = createTestTemplate(workspaceId)
    repository.register(db, template)

    repository.delete(db, workspaceId, template.id)

    val result = repository.getById(db, workspaceId, template.id)
    result should be(None)
  }

  it should "only delete templates for specified workspace" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val template1 = createTestTemplate(workspaceId1)
    val template2 = createTestTemplate(workspaceId2)

    repository.register(db, template1)
    repository.register(db, template2)

    repository.delete(db, workspaceId1, template1.id)

    val (templates1, _) = repository.gets(db, workspaceId1, 0, 10)
    val (templates2, _) = repository.gets(db, workspaceId2, 0, 10)

    templates1 should be(empty)
    templates2 should have size 1
  }

  "ReportTemplateRepository" should "handle different storage types" in {
    val workspaceId = IDs.ulid()

    val localTemplate = createTestTemplate(workspaceId, "Local Template", StorageTypes.Local)
    val s3Template = createTestTemplate(workspaceId, "S3 Template", StorageTypes.S3)

    repository.register(db, localTemplate)
    repository.register(db, s3Template)

    val retrievedLocal = repository.getById(db, workspaceId, localTemplate.id)
    val retrievedS3 = repository.getById(db, workspaceId, s3Template.id)

    retrievedLocal.get.storageType should equal(StorageTypes.Local)
    retrievedS3.get.storageType should equal(StorageTypes.S3)
  }

  it should "handle special characters in filename" in {
    val workspaceId = IDs.ulid()
    val template = createTestTemplate(workspaceId).copy(
      name = "Template's \"Special\" Name!",
      filePath = "/path/to/file with spaces & special.xlsx",
    )

    val result = repository.register(db, template)

    result should be(defined)
    result.get.name should equal("Template's \"Special\" Name!")
    result.get.filePath should equal("/path/to/file with spaces & special.xlsx")
  }

  it should "handle very large file sizes" in {
    val workspaceId = IDs.ulid()
    val largeFileSize = 1024L * 1024L * 1024L // 1 GB
    val template = createTestTemplate(workspaceId).copy(fileSize = largeFileSize)

    val result = repository.register(db, template)

    result should be(defined)
    result.get.fileSize should equal(largeFileSize)
  }

  it should "handle zero file size" in {
    val workspaceId = IDs.ulid()
    val template = createTestTemplate(workspaceId).copy(fileSize = 0L)

    val result = repository.register(db, template)

    result should be(defined)
    result.get.fileSize should equal(0L)
  }

  it should "handle various file extensions" in {
    val workspaceId = IDs.ulid()

    val xlsTemplate =
      createTestTemplate(workspaceId, "XLS Template").copy(filePath = "/path/template.xls")
    val xlsxTemplate =
      createTestTemplate(workspaceId, "XLSX Template").copy(filePath = "/path/template.xlsx")
    val pdfTemplate =
      createTestTemplate(workspaceId, "PDF Template").copy(filePath = "/path/template.pdf")

    repository.register(db, xlsTemplate)
    repository.register(db, xlsxTemplate)
    repository.register(db, pdfTemplate)

    val (templates, count) = repository.gets(db, workspaceId, 0, 10)

    templates should have size 3
    count should equal(3)
  }

  it should "handle long filenames" in {
    val workspaceId = IDs.ulid()
    val longFilename = "/very/long/path/" + ("a" * 200) + ".xlsx"
    val template = createTestTemplate(workspaceId).copy(filePath = longFilename)

    val result = repository.register(db, template)

    result should be(defined)
    result.get.filePath should equal(longFilename)
  }
}
