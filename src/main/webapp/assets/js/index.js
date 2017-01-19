(function(){
   $("ul.nav.nav-tabs li a").on("click", function(e){
       $("ul.nav.nav-tabs li").removeClass("active");
       var li = $(this).parent();
       $(li).addClass("active");

       if ($(li).hasClass("sign-in")) {
           $("#sign-in-context").show();
           $("#sign-up-context").hide();
       } else {
           $("#sign-in-context").hide();
           $("#sign-up-context").show();
       }
   });
}).call(this);