$(function(){
   $("ul.nav.nav-tabs li a").on("click", function(e){
       e.preventDefault();
       $(this).tab('show');
   });
});