package jp.ijufumi.openreports.vo.response

case class MemberResponse(
    id: Int,
    email: String,
    name: String,
    apiToken: String = "",
)
