package jp.ijufumi.openreports.services

trait LoginService {
  def login(email: String, password: String)
}
