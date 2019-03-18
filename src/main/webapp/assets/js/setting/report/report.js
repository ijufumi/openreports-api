$(function () {
  // レポート登録画面
  $(".private-setting-report-register #submitBtn").on("click", function (e) {
    var reportName = $("#reportName").val();
    var description = $("#description").val();
    var templateId = $("#templateId").val();
    var groups = [];
    $("input[name=group]:checkbox").each(function(){
      if ($(this).prop("checked") === true) {
        groups.push(Number($(this).val()));
      }
    });
    var reportInfo = {
      reportName : reportName,
      description: description,
      templateId: Number(templateId),
      groups: groups
    };

    $.ajax({
      url: "/private/settings/report/register",
      method: "post",
      data: JSON.stringify(reportInfo),
      contentType: "application/json; charset=UTF-8",
      complete: function(jqXHR, textStatus){
        console.log("complete:" + jqXHR);
      },
      success: function(data, textStatus, jqXHR) {
        console.log("success:" + jqXHR);
      },
      error: function(jqXHR, textStatus, errorThrown){
        console.log("error:" + textStatus);
      }
    });
  });

  // レポート更新画面
  $(".private-setting-report-update #submitBtn").on("click", function (e) {
    var reportId = $("#reportId").val();
    var reportName = $("#reportName").val();
    var description = $("#description").val();
    var templateId = $("#templateId").val();
    var versions = $("#versions").val();
    var groups = [];
    $("input[name=group]:checkbox").each(function(){
      if ($(this).prop("checked") === true) {
        groups.push(Number($(this).val()));
      }
    });
    var reportInfo = {
      reportName : reportName,
      description: description,
      templateId: Number(templateId),
      versions: Number(versions),
      groups: groups
    };

    $.ajax({
      url: "/private/settings/report/update/" + reportId,
      method: "post",
      data: JSON.stringify(reportInfo),
      contentType: "application/json; charset=UTF-8",
      complete: function(jqXHR, textStatus){
        console.log("complete:" + jqXHR);
      },
      success: function(data, textStatus, jqXHR) {
        console.log("success:" + jqXHR);
      },
      error: function(jqXHR, textStatus, errorThrown){
        console.log("error:" + textStatus);
      }
    });
  });

  // レポートパラメータ設定
  $(".private-setting-report-param-rel div.actions button").each(function(idx){
    $(this).on("click", function(e){
      $(this).parent().parent().parent().parent().remove();
    });
  });

  $(".private-setting-report-param-rel #addParamBtn").on("click", function(e) {
    var row = $("#tr-template").clone(true);
    row.removeClass("is-hidden");
    //$("#params").append(row);
    $("#params").append(row);
  });

  $(".private-setting-report-param-rel #submitBtn").on("click", function (e) {
    var params = [];
    $("#params tr:not(.is-hidden)").each(function(){
      var pageNo = $("input[name=pageNo]", this).val();
      var seq = $("input[name=seq]", this).val();
      var paramId = $("select[name=paramId]", this).val();
      var param = {
        pageNo: Number(pageNo),
        seq: Number(seq),
        paramId: Number(paramId),
      };
      params.push(param)
    });

    var form = $("#paramsForm");

    $("<input>", {
      type: "hidden",
      name: "params",
      value: JSON.stringify(params)
    }).appendTo(form);

    form.submit();
  })
});
