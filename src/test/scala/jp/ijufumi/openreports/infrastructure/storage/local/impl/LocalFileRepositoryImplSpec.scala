package jp.ijufumi.openreports.infrastructure.storage.local.impl

import jp.ijufumi.openreports.configs.Config
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll}

import java.nio.file.{Files, Path, Paths}
import scala.util.Try

class LocalFileRepositoryImplSpec extends AnyFlatSpec with Matchers with BeforeAndAfterEach with BeforeAndAfterAll {

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
      Files.walk(tempDir)
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

  // Note: The following tests require file system operations and proper cleanup
  // They are commented out as they depend on Config.TEMPLATE_ROOT_PATH being writable
  // For integration tests, ensure proper setup and teardown

  /*
  "create" should "create file in correct location" in {
    val repository = new LocalFileRepositoryImpl()
    val key = "created-file.txt"
    val sourceFile = Files.createTempFile(tempDir, "source", ".txt")
    Files.write(sourceFile, "test content".getBytes)

    repository.create(testWorkspaceId, key, sourceFile)

    val expectedPath = Paths.get(Config.TEMPLATE_ROOT_PATH, testWorkspaceId, key)
    Files.exists(expectedPath) should be(true)
    Files.readString(expectedPath) should equal("test content")

    // Cleanup
    Files.deleteIfExists(expectedPath)
    Files.deleteIfExists(sourceFile)
  }

  it should "create parent directories if they don't exist" in {
    val repository = new LocalFileRepositoryImpl()
    val key = "deeply/nested/file.txt"
    val sourceFile = Files.createTempFile(tempDir, "source", ".txt")
    Files.write(sourceFile, "nested content".getBytes)

    repository.create(testWorkspaceId, key, sourceFile)

    val expectedPath = Paths.get(Config.TEMPLATE_ROOT_PATH, testWorkspaceId, key)
    Files.exists(expectedPath) should be(true)

    // Cleanup
    Files.deleteIfExists(expectedPath)
    Files.deleteIfExists(sourceFile)
  }
  */
}
