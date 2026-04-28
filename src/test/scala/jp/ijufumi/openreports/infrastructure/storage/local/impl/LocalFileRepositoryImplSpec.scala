package jp.ijufumi.openreports.infrastructure.storage.local.impl

import jp.ijufumi.openreports.configs.Config
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import java.nio.file.{Files, Path, Paths}
import scala.util.Try

class LocalFileRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterEach
    with BeforeAndAfterAll {

  var tempDir: Path = _
  var testWorkspaceId: String = _

  override def beforeAll(): Unit = {
    super.beforeAll()
    // Create a temporary directory for testing
    tempDir = Files.createTempDirectory("local-file-repo-test")
    testWorkspaceId = "test-workspace-id"
  }

  override def afterAll(): Unit = {
    super.afterAll()
    // Clean up temporary directory
    Try {
      Files
        .walk(tempDir)
        .sorted(java.util.Comparator.reverseOrder())
        .forEach(Files.delete(_))
    }
  }

  "LocalFileRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new LocalFileRepositoryImpl()
  }

  "get" should "return path for given workspace and key" in {
    val repository = new LocalFileRepositoryImpl()
    val key = "test-file.txt"

    val path = repository.get(testWorkspaceId, key)

    path.toString should include(Config.TEMPLATE_ROOT_PATH)
    path.toString should include(testWorkspaceId)
    path.toString should include(key)
  }

  it should "construct correct path with subdirectories" in {
    val repository = new LocalFileRepositoryImpl()
    val key = "subdir/test-file.txt"

    val path = repository.get(testWorkspaceId, key)

    path.toString should include("subdir")
    path.toString should include("test-file.txt")
  }

  "create" should "create file in correct location" in {
    val templateRootPath = tempDir.toString

    val repository = new LocalFileRepositoryImpl()(templateRootPath = templateRootPath)
    val key = "created-file.txt"
    val sourceFile = Files.createTempFile(tempDir, "source", ".txt")
    Files.write(sourceFile, "test content".getBytes)

    repository.create(testWorkspaceId, key, sourceFile)

    val expectedPath = Paths.get(tempDir.toString, testWorkspaceId, key)
    Files.exists(expectedPath) should be(true)
    new String(Files.readAllBytes(expectedPath)) should equal("test content")

    // Cleanup
    Files.deleteIfExists(expectedPath)
    Files.deleteIfExists(sourceFile)
  }

  it should "create parent directories if they don't exist" in {
    val templateRootPath = tempDir.toString

    val repository = new LocalFileRepositoryImpl()(templateRootPath = templateRootPath)
    val key = "deeply/nested/file.txt"
    val sourceFile = Files.createTempFile(tempDir, "source", ".txt")
    Files.write(sourceFile, "nested content".getBytes)

    repository.create(testWorkspaceId, key, sourceFile)

    val expectedPath = Paths.get(tempDir.toString, testWorkspaceId, key)
    Files.exists(expectedPath) should be(true)
    Files.exists(expectedPath.getParent) should be(true)
    Files.exists(expectedPath.getParent.getParent) should be(true)

    // Cleanup
    Files
      .walk(Paths.get(tempDir.toString, testWorkspaceId))
      .sorted(java.util.Comparator.reverseOrder())
      .forEach(Files.delete(_))
    Files.deleteIfExists(sourceFile)
  }

  "get" should "reject path traversal in key" in {
    val templateRootPath = tempDir.toString
    val repository = new LocalFileRepositoryImpl()(templateRootPath = templateRootPath)

    an[IllegalArgumentException] should be thrownBy
      repository.get(testWorkspaceId, "../../../etc/passwd")
  }

  it should "reject path traversal in workspaceId" in {
    val templateRootPath = tempDir.toString
    val repository = new LocalFileRepositoryImpl()(templateRootPath = templateRootPath)

    an[IllegalArgumentException] should be thrownBy
      repository.get("../escape", "file.txt")
  }

  "create" should "reject path traversal in key" in {
    val templateRootPath = tempDir.toString
    val repository = new LocalFileRepositoryImpl()(templateRootPath = templateRootPath)
    val sourceFile = Files.createTempFile(tempDir, "src", ".txt")
    Files.write(sourceFile, "x".getBytes)

    try {
      an[IllegalArgumentException] should be thrownBy
        repository.create(testWorkspaceId, "../../escape.txt", sourceFile)
    } finally {
      Files.deleteIfExists(sourceFile)
    }
  }

  it should "overwrite existing file" in {
    val templateRootPath = tempDir.toString

    val repository = new LocalFileRepositoryImpl()(templateRootPath = templateRootPath)
    val key = "overwrite-file.txt"

    // Create first file
    val sourceFile1 = Files.createTempFile(tempDir, "source1", ".txt")
    Files.write(sourceFile1, "original content".getBytes)
    repository.create(testWorkspaceId, key, sourceFile1)

    val expectedPath = Paths.get(tempDir.toString, testWorkspaceId, key)
    new String(Files.readAllBytes(expectedPath)) should equal("original content")

    // Overwrite with second file
    val sourceFile2 = Files.createTempFile(tempDir, "source2", ".txt")
    Files.write(sourceFile2, "new content".getBytes)

    // Delete the existing file first, then create
    Files.deleteIfExists(expectedPath)
    repository.create(testWorkspaceId, key, sourceFile2)

    new String(Files.readAllBytes(expectedPath)) should equal("new content")

    // Cleanup
    Files.deleteIfExists(expectedPath)
    Files.deleteIfExists(sourceFile1)
    Files.deleteIfExists(sourceFile2)
    Files
      .walk(Paths.get(tempDir.toString, testWorkspaceId))
      .sorted(java.util.Comparator.reverseOrder())
      .forEach(Files.delete(_))
  }
}
