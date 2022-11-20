package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

object queries {
  val reportQuery = TableQuery[Reports]
  val dataSourceQuery = TableQuery[DataSources]
  val driverTypeQuery = TableQuery[DriverTypes]
  val memberQuery = TableQuery[Members]
  val templateQuery = TableQuery[Templates]
  val workspaceMemberQuery = TableQuery[WorkspaceMembers]
  val workspaceQuery = TableQuery[Workspaces]
  val storageQuery = TableQuery[Storages]
}
