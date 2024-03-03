package jp.ijufumi.openreports.presentation.models.responses

case class Member(
    id: String,
    email: String,
    name: String,
    apiToken: String = "",
    workspaces: Seq[Workspace] = Seq.empty,
) {
  def withApiToken(apiToken: String): Member = {
    copy(apiToken = apiToken)
  }
  def withWorkspace(workspaces: Seq[Workspace]): Member = {
    copy(workspaces = workspaces)
  }

}
