$(function() {

    $('#side-menu').metisMenu();

});

//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse');
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse');
        }

        height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    });

    var url = window.location;
    var element = $('ul.nav a').filter(function() {
    	var m = this.href == url || url.href.indexOf(this.href) == 0;
        return m;
    }).addClass('active').parent().parent().addClass('in').parent();
   
    if (element.is('li')) {
        element.addClass('active');
    }
});


$(document).ready(function() {
	  jQuery.validator.setDefaults({
	    errorPlacement: function(error, element) {
	      // if the input has a prepend or append element, put the validation msg after the parent div
	      if(element.parent().hasClass('input-prepend') || element.parent().hasClass('input-append')) {
	        error.insertAfter(element.parent());		
	      // else just place the validation message immediatly after the input
	      } else {
	        error.insertAfter(element);
	      }
	    },
	    errorElement: "small", // contain the error msg in a small tag
	    wrapper: "div", // wrap the error message and small tag in a div
	    highlight: function(element) {
	      $(element).closest('.control-group').addClass('error'); // add the Bootstrap error class to the control group
	    },
	    success: function(element) {
	      $(element).closest('.control-group').removeClass('error'); // remove the Boostrap error class from the control group
	    }
	  });
	  
	  $(':input').change(function() {
		  if (this.nodeName != "SELECT")
		    $(this).val($(this).val().trim());
		});
	 
	});

function injectTrim(handler) {
	  return function (element, event) {
	    if (element.tagName === "textarea" || (element.tagName === "input" 
	                                       && element.type !== "password")) {
	      element.value = $.trim(element.value);
	    }
	    return handler.call(this, element, event);
	  };
	 }

