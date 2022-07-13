package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.MemberResponse

trait LoginService {
  def login(email: String, password: String): Option[MemberResponse]
}
