<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <jsp:include page="include/header.jsp" />
 
</head>
<body>
 <div id="wrapper">
       <jsp:include page="include/navigationBar.jsp"/>
	    	
        <!-- Page Content -->
        <div id="page-wrapper">
        <div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Add administrator</h1>
				</div>
				<!-- /.col-lg-12 -->
			</div>
            <div class="row">
				<div class="container">
        <div class="row">
            <div class="col-md-6 col-md-offset-2">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">New user!</h3>
                    </div>
                    <div class="panel-body">
                        <form method="post" action="Register" id=formRegister role="form">
                            <fieldset>
                                <div class="form-group">
                                    <br />
                                    <input class="form-control" placeholder="Username" id="username" name="username" type="text" autofocus  >
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Password" name="Pwd" type="password" id="Pwd" > 
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Confirmed Password"  name="ConfirmedPwd" id="ConfirmedPwd" type="password" >
                                </div>
                                <!-- Change this to a button or input when using this as a form -->
                                <button id="boton" class="btn btn-lg btn-primary btn-block" >Add Admin</button>
                            </fieldset>
                        </form> 
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="dist/js/comparePassword.js"></script>
            </div>
   
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->
    </div>                                                                                                                                     
    <jsp:include page="include/footer.jsp" />
    <script src="dist/libs/ace/ace.js"></script>
	<script src="dist/libs/jquery/dist/jquery.validate.min.js"></script>
</body>
</html>