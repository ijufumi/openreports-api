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

    var form = $("#reportForm");

    $("<input>", {
      type: "hidden",
      name: "reportInfo",
      value: JSON.stringify(reportInfo)
    }).appendTo(form);

    form.submit();
  });

  // レポート更新画面
  $(".private-setting-report-update #submitBtn").on("click", function (e) {
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

    var form = $("#reportForm");

    $("<input>", {
      type: "hidden",
      name: "reportInfo",
      value: JSON.stringify(reportInfo)
    }).appendTo(form);

    form.submit();
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
