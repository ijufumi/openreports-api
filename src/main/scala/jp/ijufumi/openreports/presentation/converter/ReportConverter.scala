package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.domain.models.entity.{
  Report => ReportModel,
  ReportGroup => ReportGroupModel,
  ReportTemplate => ReportTemplateModel,
}
import jp.ijufumi.openreports.presentation.response.{
  Report => ReportResponse,
  ReportGroup => ReportGroupResponse,
  ReportTemplate => ReportTemplateResponse,
}
import jp.ijufumi.openreports.presentation.request.{
  CreateReport,
  CreateReportGroup,
  CreateTemplate,
  UpdateReport,
  UpdateReportGroup,
  UpdateTemplate,
}
import jp.ijufumi.openreports.usecase.port.input.param.{
  CreateReportGroupInput,
  CreateReportInput,
  CreateTemplateInput,
  UpdateReportGroupInput,
  UpdateReportInput,
  UpdateTemplateInput,
}

object ReportConverter {
  def toResponse(model: ReportModel): ReportResponse = {
    ReportResponse(
      model.id,
      model.name,
      model.templateId,
      model.dataSourceId,
      model.createdAt,
      model.updatedAt,
      model.reportTemplate.map(toTemplateResponse),
    )
  }

  def toTemplateResponse(model: ReportTemplateModel): ReportTemplateResponse = {
    ReportTemplateResponse(
      model.id,
      model.name,
      model.filePath,
      model.storageType,
      model.fileSize,
      model.createdAt,
      model.updatedAt,
    )
  }

  def toGroupResponse(model: ReportGroupModel): ReportGroupResponse = {
    ReportGroupResponse(
      model.id,
      model.name,
      model.createdAt,
      model.updatedAt,
    )
  }

  def toCreateReportInput(request: CreateReport): CreateReportInput = {
    CreateReportInput(request.name, request.templateId, request.dataSourceId, request.parameterIds)
  }

  def toUpdateReportInput(request: UpdateReport): UpdateReportInput = {
    UpdateReportInput(request.name, request.templateId, request.dataSourceId)
  }

  def toCreateTemplateInput(request: CreateTemplate): CreateTemplateInput = {
    CreateTemplateInput(request.name)
  }

  def toUpdateTemplateInput(request: UpdateTemplate): UpdateTemplateInput = {
    UpdateTemplateInput(request.name)
  }

  def toCreateGroupInput(request: CreateReportGroup): CreateReportGroupInput = {
    CreateReportGroupInput(request.name, request.reportIds)
  }

  def toUpdateGroupInput(request: UpdateReportGroup): UpdateReportGroupInput = {
    UpdateReportGroupInput(request.name, request.reportIds)
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportResponse(model: ReportModel): ReportResponse =
      ReportConverter.toResponse(model)
    implicit def toReportResponse2(model: Option[ReportModel]): Option[ReportResponse] =
      model.map(ReportConverter.toResponse)
    implicit def toReportResponses(model: Seq[ReportModel]): Seq[ReportResponse] =
      model.map(ReportConverter.toResponse)
    implicit def toTemplateResp(model: ReportTemplateModel): ReportTemplateResponse =
      ReportConverter.toTemplateResponse(model)
    implicit def toTemplateResp2(
        model: Option[ReportTemplateModel],
    ): Option[ReportTemplateResponse] = model.map(ReportConverter.toTemplateResponse)
    implicit def toTemplateResps(model: Seq[ReportTemplateModel]): Seq[ReportTemplateResponse] =
      model.map(ReportConverter.toTemplateResponse)
    implicit def toGroupResp(model: ReportGroupModel): ReportGroupResponse =
      ReportConverter.toGroupResponse(model)
    implicit def toGroupResp2(model: Option[ReportGroupModel]): Option[ReportGroupResponse] =
      model.map(ReportConverter.toGroupResponse)
    implicit def toGroupResps(model: Seq[ReportGroupModel]): Seq[ReportGroupResponse] =
      model.map(ReportConverter.toGroupResponse)
  }
}
