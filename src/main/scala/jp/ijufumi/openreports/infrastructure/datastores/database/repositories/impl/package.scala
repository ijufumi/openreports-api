package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{DataSources, DriverTypes, Functions, Members, RefreshTokens, ReportGroupReports, ReportGroups, ReportParameters, ReportReportParameters, Reports, RoleFunctions, Roles, StorageS3s, ReportTemplates, WorkspaceMembers, Workspaces}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration

package object impl {
  val queryTimeout: Duration = Duration("10s")

  val reportQuery = TableQuery[Reports]
  val reportGroupQuery = TableQuery[ReportGroups]
  val reportGroupReportQuery = TableQuery[ReportGroupReports]
  val reportParameterQuery = TableQuery[ReportParameters]
  val reportReportParameterQuery = TableQuery[ReportReportParameters]
  val dataSourceQuery = TableQuery[DataSources]
  val driverTypeQuery = TableQuery[DriverTypes]
  val memberQuery = TableQuery[Members]
  val templateQuery = TableQuery[ReportTemplates]
  val workspaceMemberQuery = TableQuery[WorkspaceMembers]
  val workspaceQuery = TableQuery[Workspaces]
  val storageQuery = TableQuery[StorageS3s]
  val roleQuery = TableQuery[Roles]
  val functionQuery = TableQuery[Functions]
  val roleFunctionQuery = TableQuery[RoleFunctions]
  val refreshTokenQuery = TableQuery[RefreshTokens]
}
