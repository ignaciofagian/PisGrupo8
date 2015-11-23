$(document).ready(function() {

  
  $("#btnLogin").click(function() {
	  $('#formLogin').validate(
			  {
			    rules: {
			      password: {
			        minlength: 3,
			        required: true,
			      },
			      username: {
			        required: true,
			      }
			    }
	 });
	  
	 if ($('#formLogin').valid()) {
		 document.getElementById('formLogin').submit();
	 } 
	});
});


