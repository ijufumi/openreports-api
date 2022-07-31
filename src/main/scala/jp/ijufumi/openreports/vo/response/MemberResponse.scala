package jp.ijufumi.openreports.vo.response

case class MemberResponse(
    id: Int,
    emailAddress: String,
    name: String,
    isAdmin: String,
    apiToken: String = "",
)
