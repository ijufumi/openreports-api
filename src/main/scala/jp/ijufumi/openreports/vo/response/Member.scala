package jp.ijufumi.openreports.vo.response

case class Member(
    id: String,
    email: String,
    name: String,
    apiToken: String = "",
)
