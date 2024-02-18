package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.entities._
import slick.jdbc.PostgresProfile.api._

private[impl] object queries {
  val reportQuery = TableQuery[Reports]
  val reportGroupQuery = TableQuery[ReportGroups]
  val reportGroupReportQuery = TableQuery[ReportGroupReports]
  val reportParameterQuery = TableQuery[ReportParameters]
  val reportReportParameterQuery = TableQuery[ReportReportParameters]
  val dataSourceQuery = TableQuery[DataSources]
  val driverTypeQuery = TableQuery[DriverTypes]
  val memberQuery = TableQuery[Members]
  val templateQuery = TableQuery[Templates]
  val workspaceMemberQuery = TableQuery[WorkspaceMembers]
  val workspaceQuery = TableQuery[Workspaces]
  val storageQuery = TableQuery[StorageS3s]
  val roleQuery = TableQuery[Roles]
  val functionQuery = TableQuery[Functions]
  val roleFunctionQuery = TableQuery[RoleFunctions]
}
