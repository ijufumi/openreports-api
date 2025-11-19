package jp.ijufumi.openreports.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters._

class TemporaryFilesSpec extends AnyFlatSpec with Matchers {

  "TemporaryFiles.createDir" should "create a temporary directory" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      // Directory should exist in /tmp
      val fullPath = Path.of("/tmp", tempDir.path().toString)
      Files.exists(fullPath) should be(true)
      Files.isDirectory(fullPath) should be(true)
    } finally {
      tempDir.close()
    }
  }

  it should "create directories with unique names" in {
    val dir1 = TemporaryFiles.createDir()
    val dir2 = TemporaryFiles.createDir()

    try {
      dir1.path().toString should not equal dir2.path().toString
    } finally {
      dir1.close()
      dir2.close()
    }
  }

  "TemporaryDir.close" should "delete the directory" in {
    val tempDir = TemporaryFiles.createDir()
    val fullPath = Path.of("/tmp", tempDir.path().toString)

    Files.exists(fullPath) should be(true)

    tempDir.close()

    Files.exists(fullPath) should be(false)
  }

  it should "be usable with try-with-resources" in {
    var dirPath: Path = null

    val dir = TemporaryFiles.createDir()
    dirPath = Path.of("/tmp", dir.path().toString)
    dir.close()

    // Directory should be cleaned up
    Files.exists(dirPath) should be(false)
  }

  "TemporaryFiles.createFile" should "create a file in specified directory" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        Files.exists(tempFile.path()) should be(true)
        Files.isRegularFile(tempFile.path()) should be(true)
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "create files with unique names in the same directory" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val file1 = TemporaryFiles.createFile(fullDirPath)
      val file2 = TemporaryFiles.createFile(fullDirPath)

      try {
        file1.path().getFileName.toString should not equal file2.path().getFileName.toString
      } finally {
        file1.close()
        file2.close()
        Files.deleteIfExists(file1.path())
        Files.deleteIfExists(file2.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "create files with specified suffix" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath, suffix = ".txt")

      try {
        tempFile.path().getFileName.toString should endWith(".txt")
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "support various file extensions" in {
    val tempDir = TemporaryFiles.createDir()
    val extensions = Seq(".txt", ".json", ".xml", ".csv", ".pdf")

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)

      extensions.foreach { ext =>
        val tempFile = TemporaryFiles.createFile(fullDirPath, suffix = ext)
        try {
          tempFile.path().getFileName.toString should endWith(ext)
        } finally {
          tempFile.close()
          Files.deleteIfExists(tempFile.path())
        }
      }
    } finally {
      tempDir.close()
    }
  }

  "TemporaryFile.write" should "write data to file" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        val testData = "Hello, World!".getBytes("UTF-8")
        tempFile.write(testData)

        val writtenData = Files.readAllBytes(tempFile.path())
        new String(writtenData, "UTF-8") should equal("Hello, World!")
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "overwrite existing data by default" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        tempFile.write("First write".getBytes("UTF-8"))
        tempFile.write("Second write".getBytes("UTF-8"))

        val writtenData = Files.readAllBytes(tempFile.path())
        new String(writtenData, "UTF-8") should equal("Second write")
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "append data when append=true" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        tempFile.write("First ".getBytes("UTF-8"))
        tempFile.write("Second".getBytes("UTF-8"), append = true)

        val writtenData = Files.readAllBytes(tempFile.path())
        new String(writtenData, "UTF-8") should equal("First Second")
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "handle binary data" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        val binaryData = Array[Byte](0x00, 0x01, 0x02, 0xFF.toByte, 0xFE.toByte)
        tempFile.write(binaryData)

        val writtenData = Files.readAllBytes(tempFile.path())
        writtenData should equal(binaryData)
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "handle empty data" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        tempFile.write(Array.empty[Byte])

        Files.size(tempFile.path()) should equal(0L)
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "handle large data" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        val largeData = Array.fill[Byte](1024 * 1024)(0x42) // 1MB of 'B'
        tempFile.write(largeData)

        Files.size(tempFile.path()) should equal(1024L * 1024L)
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "handle unicode data" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        val unicodeText = "こんにちは世界 🌍"
        tempFile.write(unicodeText.getBytes("UTF-8"))

        val writtenData = Files.readAllBytes(tempFile.path())
        new String(writtenData, "UTF-8") should equal(unicodeText)
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  "TemporaryFile.close" should "delete file when deleteOnClose is true" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath, deleteOnClose = true)
      val filePath = tempFile.path()

      Files.exists(filePath) should be(true)

      tempFile.close()

      Files.exists(filePath) should be(false)
    } finally {
      tempDir.close()
    }
  }

  it should "preserve file when deleteOnClose is false" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath, deleteOnClose = false)
      val filePath = tempFile.path()

      Files.exists(filePath) should be(true)

      tempFile.close()

      Files.exists(filePath) should be(true)

      // Clean up
      Files.deleteIfExists(filePath)
    } finally {
      tempDir.close()
    }
  }

  "TemporaryFile.path" should "return full path to the file" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val tempFile = TemporaryFiles.createFile(fullDirPath)

      try {
        val path = tempFile.path()
        path.getParent.getFileName.toString should equal(tempDir.path().toString)
        Files.exists(path) should be(true)
      } finally {
        tempFile.close()
        Files.deleteIfExists(tempFile.path())
      }
    } finally {
      tempDir.close()
    }
  }

  "TemporaryFiles" should "support multiple files in same directory" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val file1 = TemporaryFiles.createFile(fullDirPath, suffix = ".txt")
      val file2 = TemporaryFiles.createFile(fullDirPath, suffix = ".json")
      val file3 = TemporaryFiles.createFile(fullDirPath, suffix = ".xml")

      try {
        file1.write("Text content".getBytes("UTF-8"))
        file2.write("{\"key\":\"value\"}".getBytes("UTF-8"))
        file3.write("<root/>".getBytes("UTF-8"))

        // All files should exist
        Files.exists(file1.path()) should be(true)
        Files.exists(file2.path()) should be(true)
        Files.exists(file3.path()) should be(true)

        // All should be in the same directory
        file1.path().getParent should equal(file2.path().getParent)
        file2.path().getParent should equal(file3.path().getParent)
      } finally {
        file1.close()
        file2.close()
        file3.close()
        Files.deleteIfExists(file1.path())
        Files.deleteIfExists(file2.path())
        Files.deleteIfExists(file3.path())
      }
    } finally {
      tempDir.close()
    }
  }

  it should "handle concurrent file creation" in {
    val tempDir = TemporaryFiles.createDir()

    try {
      val fullDirPath = Path.of("/tmp", tempDir.path().toString)
      val files = (1 to 10).map { _ =>
        TemporaryFiles.createFile(fullDirPath)
      }

      try {
        // All files should have unique names
        val fileNames = files.map(_.path().getFileName.toString)
        fileNames.distinct.size should equal(10)

        // All files should exist
        files.foreach { file =>
          Files.exists(file.path()) should be(true)
        }
      } finally {
        files.foreach { file =>
          file.close()
          Files.deleteIfExists(file.path())
        }
      }
    } finally {
      tempDir.close()
    }
  }
}
