<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 	  <jsp:include page="include/header.jsp" />
 	   
</head>
<body>
	<div id="wrapper">
	    <jsp:include page="include/navigationBar.jsp">
	    	<jsp:param name="pageName" value="index" />
	 	 </jsp:include>
	    	
	      <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">Home</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-md-10">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-users fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div id="label-users-count" class="huge"></div>
                                        <div>Total users!</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-10">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-briefcase fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div id="label-packets-count" class="huge"></div>
                                        <div>Total packets!</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-10">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-bar-chart-o fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div id="label-stocks-count" class="huge"></div>
                                        <div>Stocks!</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">Packets</h1>
                    </div>
                </div>
             
              
              <!-- pagination aquiiiii -->
              
            </div>
	  </div>
 	 </div>
   	  <jsp:include page="include/footer.jsp" />
   	  <script src="dist/libs/jquery/dist/jquery.validate.min.js"></script>
   	  <script src="dist/js/home.js"></script>
</body>
</html>