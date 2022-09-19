package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

object queries {
  val reportQuery = TableQuery[Reports]
  val dataSourceQuery = TableQuery[DataSources]
  val driverTypeQuery = TableQuery[DriverTypes]
  val memberQuery = TableQuery[Members]
  val reportTemplateQuery = TableQuery[ReportTemplates]
  val workspaceMemberQuery = TableQuery[WorkspaceMembers]
  val workspaceQuery = TableQuery[Workspaces]
}
