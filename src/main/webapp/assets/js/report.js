(function () {
  var postData = $("#paramForm").serialize();

  $("#nextPage").on('click', function () {
    $.ajax({
      url: "/private/report/params",
      method: "post",
      contentType: "application/json",
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
});
