package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import jp.ijufumi.openreports.entities._
import slick.jdbc.PostgresProfile.api._

private[impl] object queries {
  val reportQuery = TableQuery[Reports]
  val dataSourceQuery = TableQuery[DataSources]
  val driverTypeQuery = TableQuery[DriverTypes]
  val memberQuery = TableQuery[Members]
  val templateQuery = TableQuery[Templates]
  val workspaceMemberQuery = TableQuery[WorkspaceMembers]
  val workspaceQuery = TableQuery[Workspaces]
  val storageQuery = TableQuery[Storages]
  val permissionQuery = TableQuery[Permissions]
}
