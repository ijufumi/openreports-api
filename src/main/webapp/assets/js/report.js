$(function () {
  $("#nextPage").on('click', function () {
    var postData = $("#paramForm").serialize();
    $.ajax({
      url: "/private/report/params",
      method: "post",
      data: postData
    })
      .fail(function (jqXHR, status, ex) {
        console.log("status:" + status);
        console.log("error:" + ex);
      })
      .done(function (data) {
        console.log(data);
      })
    ;
  });

  $("#printOutBtn").on('click', function () {
    var postData = $("#paramForm").serialize();
    console.log("postData:" + postData);
    $.ajax({
      url: "/private/report/params",
      method: "post",
      data: postData
    })
      .fail(function (jqXHR, status, ex) {
        console.log("status:" + status);
        console.log("error:" + ex);
      })
      .done(function (data) {
        console.log(data);
        window.location.href="/private/report/printOut/" + $("#reportId").val();
      })
    ;
  });
});
