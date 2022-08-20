package jp.ijufumi.openreports.vo.response

case class MemberResponse(
    id: String,
    email: String,
    name: String,
    apiToken: String = "",
)
