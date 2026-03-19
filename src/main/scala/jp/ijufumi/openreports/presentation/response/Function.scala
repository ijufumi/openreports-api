package jp.ijufumi.openreports.presentation.response

import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes

case class Function(resource: String, action: ActionTypes.ActionType)
