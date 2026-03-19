package jp.ijufumi.openreports.presentation.response

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
)
