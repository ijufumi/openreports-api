package jp.ijufumi.openreports.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

class StringsSpec extends AnyFlatSpec with Matchers {

  "generateRandomString" should "generate string with correct length" in {
    val result = Strings.generateRandomString(10)()

    result should have length 10
  }

  it should "generate different strings on each call" in {
    val str1 = Strings.generateRandomString(20)()
    val str2 = Strings.generateRandomString(20)()

    str1 should not equal str2
  }

  it should "contain only lowercase when only useLower=true" in {
    val result =
      Strings.generateRandomString(100, useLower = true, useUpper = false, useNumeric = false)()

    result should fullyMatch regex "[a-z]+"
  }

  it should "contain only uppercase when only useUpper=true" in {
    val result =
      Strings.generateRandomString(100, useLower = false, useUpper = true, useNumeric = false)()

    result should fullyMatch regex "[A-Z]+"
  }

  it should "contain only numbers when only useNumeric=true" in {
    val result =
      Strings.generateRandomString(100, useLower = false, useUpper = false, useNumeric = true)()

    result should fullyMatch regex "[0-9]+"
  }

  it should "contain mixed characters with all flags true" in {
    val result =
      Strings.generateRandomString(100, useLower = true, useUpper = true, useNumeric = true)()

    result should fullyMatch regex "[a-zA-Z0-9]+"
  }

  it should "include extra characters when provided" in {
    val result =
      Strings.generateRandomString(50, useLower = false, useUpper = false, useNumeric = false)(
        "@#$",
      )

    result should fullyMatch regex "[@#$]+"
  }

  it should "combine base characters and extra characters" in {
    val result =
      Strings.generateRandomString(100, useLower = true, useUpper = false, useNumeric = false)("-_")

    result should fullyMatch regex "[a-z_-]+"
  }

  it should "handle count of 0" in {
    val result = Strings.generateRandomString(0)()

    result should have length 0
    result should equal("")
  }

  it should "handle count of 1" in {
    val result = Strings.generateRandomString(1)()

    result should have length 1
  }

  it should "generate very long strings" in {
    val result = Strings.generateRandomString(1000)()

    result should have length 1000
  }

  "generateQueryParamsFromMap" should "generate query string from map" in {
    val params = mutable.Map[String, String]("key1" -> "value1", "key2" -> "value2")
    val result = Strings.generateQueryParamsFromMap(params)

    result should include("key1=value1")
    result should include("key2=value2")
    result should include("&")
  }

  it should "handle single parameter" in {
    val params = mutable.Map[String, String]("key" -> "value")
    val result = Strings.generateQueryParamsFromMap(params)

    result should equal("key=value")
  }

  it should "handle empty map" in {
    val params = mutable.Map[String, String]()
    val result = Strings.generateQueryParamsFromMap(params)

    result should equal("")
  }

  it should "URL encode special characters" in {
    val params = mutable.Map[String, String]("key" -> "value with spaces")
    val result = Strings.generateQueryParamsFromMap(params)

    result should include("value+with+spaces")
  }

  "convertToBase64" should "encode string to Base64" in {
    val input = "Hello, World!"
    val result = Strings.convertToBase64(input)

    result should equal("SGVsbG8sIFdvcmxkIQ==")
  }

  it should "handle empty string" in {
    val result = Strings.convertToBase64("")

    result should not be null
  }

  it should "handle special characters" in {
    val input = "!@#$%^&*()"
    val result = Strings.convertToBase64(input)

    result should not be empty
    result should fullyMatch regex "[A-Za-z0-9+/=]+"
  }

  it should "handle unicode characters" in {
    val input = "こんにちは"
    val result = Strings.convertToBase64(input)

    result should not be empty
  }

  "convertFromBase64" should "decode Base64 string" in {
    val encoded = "SGVsbG8sIFdvcmxkIQ=="
    val result = Strings.convertFromBase64(encoded)

    result should equal("Hello, World!")
  }

  it should "handle empty Base64 string" in {
    val result = Strings.convertFromBase64("")

    result should equal("")
  }

  "convertToBase64 and convertFromBase64" should "be reversible" in {
    val original = "Test string with special chars: !@#$%"
    val encoded = Strings.convertToBase64(original)
    val decoded = Strings.convertFromBase64(encoded)

    decoded should equal(original)
  }

  it should "handle round-trip with unicode" in {
    val original = "日本語テスト"
    val encoded = Strings.convertToBase64(original)
    val decoded = Strings.convertFromBase64(encoded)

    decoded should equal(original)
  }

  "generateSlug" should "generate 10 character slug" in {
    val slug = Strings.generateSlug()

    slug should have length 10
  }

  it should "contain only lowercase letters and numbers" in {
    val slug = Strings.generateSlug()

    slug should fullyMatch regex "[a-z0-9]+"
  }

  it should "not contain uppercase letters" in {
    val slug = Strings.generateSlug()

    slug should not include regex("[A-Z]")
  }

  it should "generate different slugs" in {
    val slug1 = Strings.generateSlug()
    val slug2 = Strings.generateSlug()

    slug1 should not equal slug2
  }

  it should "generate unique slugs in succession" in {
    val slugs = (1 to 100).map(_ => Strings.generateSlug())

    slugs.distinct.size should be > 90 // Allow for small chance of collision
  }

  "nameFromEmail" should "extract name from email" in {
    val result = Strings.nameFromEmail("john.doe@example.com")

    result should equal("john.doe")
  }

  it should "handle simple email" in {
    val result = Strings.nameFromEmail("user@domain.com")

    result should equal("user")
  }

  it should "handle email with numbers" in {
    val result = Strings.nameFromEmail("user123@example.com")

    result should equal("user123")
  }

  it should "handle email with special characters in name" in {
    val result = Strings.nameFromEmail("user.name+tag@example.com")

    result should equal("user.name+tag")
  }

  it should "return original string if no @ symbol" in {
    val result = Strings.nameFromEmail("notanemail")

    result should equal("notanemail")
  }

  it should "handle empty string before @" in {
    val result = Strings.nameFromEmail("@example.com")

    result should equal("")
  }

  it should "handle multiple @ symbols (take first part)" in {
    val result = Strings.nameFromEmail("user@name@example.com")

    result should equal("user")
  }

  "extension" should "extract file extension" in {
    val result = Strings.extension("document.pdf")

    result should equal(".pdf")
  }

  it should "handle multiple dots in filename" in {
    val result = Strings.extension("archive.tar.gz")

    result should equal(".gz")
  }

  it should "return empty string if no extension" in {
    val result = Strings.extension("filename")

    result should equal("")
  }

  it should "handle file with path" in {
    val result = Strings.extension("/path/to/file.txt")

    result should equal(".txt")
  }

  it should "handle hidden files with extension" in {
    val result = Strings.extension(".gitignore")

    result should equal(".gitignore")
  }

  it should "handle filename starting with dot but having extension" in {
    val result = Strings.extension(".config.json")

    result should equal(".json")
  }

  it should "handle empty string" in {
    val result = Strings.extension("")

    result should equal("")
  }

  it should "handle various extensions" in {
    Strings.extension("file.txt") should equal(".txt")
    Strings.extension("file.xlsx") should equal(".xlsx")
    Strings.extension("file.tar.gz") should equal(".gz")
    Strings.extension("file.backup.2024.sql") should equal(".sql")
  }
}
