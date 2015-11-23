<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Login</title>
    <link href="dist/libs/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="dist/libs/bootstrap/dist/css/bootstrap-multiselect.css" rel="stylesheet" />
    <link href="dist/libs/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
    <link href="dist/css/global.css" rel="stylesheet">
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script src="dist/libs/jquery/dist/jquery.min.js"></script>
    <script src="dist/libs/jquery/dist/jquery.validate.min.js"></script>
    <script src="dist/libs/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="dist/libs/metisMenu/dist/metisMenu.min.js"></script>
    <script src="dist/js/loginForm.js"></script>
    <script src="dist/js/global.js"></script>
</head>
<body>
	<% if ((request.getAttribute("errorNumber") != null) && ((Integer)request.getAttribute("errorNumber")) != 0) { %>
	    <script type="text/javascript">
	   
	    $(document).ready(function() {
		    
	    	var username = '<%=request.getAttribute("username")%>';
	    	
	    	$('#username').val(username);
	    	var validator = $("#formLogin").validate();

			<% if (((Integer)request.getAttribute("errorNumber")) == 1) { %>
	    	
		    	validator.showErrors({
					  "username": '<%=request.getAttribute("errorText")%>'
	    		});
	    	
	    	<% } else {  %>
	    	
		    	validator.showErrors({
		    		"password" : '<%=request.getAttribute("errorText")%>'
		    	});
	       
	    	<% }%>
	   	 });
		</script>
	 <%	}%>
	    	
	<div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Please Sign In</h3>
                    </div>
                    <div class="panel-body">
                        <form  id="formLogin" action="Login" method="post" role="form">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Username" id="username"  name="username" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Password" name="password" type="password" value="">
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="Remember Me">Remember Me
                                    </label>
                                </div>
                                <!-- Change this to a button or input when using this as a form -->
                                <a href="javascript:{}" id="btnLogin"  class="btn btn-lg btn-success btn-block">Login</a>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</body>
</html>