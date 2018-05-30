$(function(){
    $("#fileDownloadButton").on("click", function(e) {
        window.location.href = "/private/report/download";
    });

    $("#fileUploadButton").on("click", function (e) {
        window.location.href = "/private/settings/report/fileUpload/form";
    });
});