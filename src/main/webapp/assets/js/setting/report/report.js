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
});
