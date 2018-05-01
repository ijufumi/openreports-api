$(function(){
    $("#logoutButton").on("click", function(e) {
        window.location.href = "/home/logout";
    })

    $("#fileDownloadButton").on("click", function(e) {
       $.get("/report/download");
    });
});