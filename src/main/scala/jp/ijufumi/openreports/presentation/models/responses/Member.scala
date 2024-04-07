package jp.ijufumi.openreports.presentation.models.responses

case class Member(
    id: String,
    email: String,
    name: String,
    workspaces: Seq[Workspace] = Seq.empty,
) {
  def withWorkspace(workspaces: Seq[Workspace]): Member = {
    copy(workspaces = workspaces)
  }

}
