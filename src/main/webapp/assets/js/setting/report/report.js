$(function () {
  $(".private-setting-report-param-rel div.actions").each(function(idx){
    if (idx === 1) {
      $("span:eq(1)", this).hide();
    } else {
      $("span:eq(1)", this).on("click", function(e){
        $(this).parent().parent().parent().remove();
      });
    }
    $("span:eq(0)", this).on("click", function(e){
      var row = $("#tr-template").clone(true);
      row.removeClass("is-hidden");
      //$("#params").append(row);
      $(this).parent().parent().parent().after(row);
    });
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
      type: "text",
      id: "params",
      value: JSON.stringify(params)
    }).appendTo(form);

    form.submit();
  })
});
