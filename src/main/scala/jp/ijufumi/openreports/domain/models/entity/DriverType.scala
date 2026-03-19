package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
)
