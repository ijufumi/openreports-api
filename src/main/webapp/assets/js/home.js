$(function(){
    $("#logoutButton").on("click", function(e) {
        window.location.href = "/home/logout";
    })

    $("#fileDownloadButton").on("click", function(e) {
       $.get("/report/download", function(d){
           console.log("response:" + d)
       })
           .done(function(d){
               console.log("done:" + d)
           })
           .fail(function(d){
               console.log("fail:" + d)
           });
    });
});