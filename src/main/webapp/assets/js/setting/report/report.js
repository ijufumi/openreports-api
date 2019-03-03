$(function () {
  $(".private-setting-report-param-rel div.actions button").each(function(idx){
    $(this).on("click", function(e){
      $(this).parent().parent().parent().parent().remove();
    });
  });

  $("#addParamBtn").on("click", function(e) {
    var row = $("#tr-template").clone(true);
    row.removeClass("is-hidden");
    //$("#params").append(row);
    $("#params").append(row);
  });

  $("#submitBtn").on("click", function (e) {
    var params = [];
    $("#params tr:not(.is-hidden)").each(function(){
      var pageNo = $("input[name=pageNo]", this).val();
      var seq = $("input[name=seq]", this).val();
      var paramId = $("select[name=paramId]", this).val();
      var param = {
        pageNo: pageNo,
        seq: seq,
        paramId: paramId,
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
