$(document).ready(function() {  
	
	  $.validator.addMethod("checkUserExists", function(value, element) {
	        return !userAlreadyEsists(value);
	    });
	
		  $("#formRegister").validate(
				  {
				    rules: {
				      Pwd: {
				        required: true,
				      },
				      ConfirmedPwd: {
				    	  required: true,
				    	  equalTo: "#Pwd"
				      },
				      username: {
				        required: true,
				        checkUserExists: true
				      }
				    },
				    messages: {
				    	ConfirmedPwd: {
				    		equalTo: "must be the same as above"
				    	},
				    	username :
				    		{
				    		checkUserExists: "Take an other username, this already exists"
				    		}
				    }
		 });
//		  validator.showErrors({
//			  "username": 'Username is required',
//			  "Pwd" : 'Password is required',
//			  "ConfirmedPwd" : 'Required and must be the same as above'
//		});

		  $("#boton").click(function() {
			 if ($("#formRegister").valid()) {
				 document.getElementById("formRegister").submit();
			 } 
		  });
});
	
function userAlreadyEsists(username) {
	  var exists = false;
	 
	  jQuery.ajax({
	    url: 'Register',
	    data : 'query=existsUser&username=' + username,
	    success: function(response) {
	      if (response == "1")
	    	  exists = true;
	      else
	    	  exists = false;
	    },
	    async:false
	  });

	  return exists;
	}
