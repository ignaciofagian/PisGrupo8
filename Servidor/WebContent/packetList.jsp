<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="include/header.jsp" />
</head>
<body>
	<div id="wrapper">
		<jsp:include page="include/navigationBar.jsp" />

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Packets</h1>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->
			<div class="row">

				<div class="col-lg-3">
					<div class="panel panel-primary">
						<div class="panel-heading">Packet list</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="table-responsive" style="height: 370px;">
							<form method="post" action="PacketList" id="DeleteForm">
								<table id="table-packet-list"
									class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Name</th>
										</tr>
									</thead>
									<tbody>

									</tbody>
								</table>
								<input type="hidden" name="deleteQ" id="deleteQ" value="test" />
							</form>
							</div>
							<!-- /.table-responsive -->
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				 <div id="packet-detail"  class="col-lg-9" style="display:none;">
				 </div>
			</div>
		</div>
	</div>
	<jsp:include page="include/footer.jsp" />
	<script src="dist/libs/ace/ace.js"></script>
	<script src="dist/libs/jquery/dist/jquery.validate.min.js"></script>
	<script src="dist/js/packetList.js"></script>
	
	<% if ((request.getAttribute("id") != null)) { %>
	    <script type="text/javascript">
	   
	    $(document).ready(function() {
	    	if ($( "#packet-" + <%=request.getAttribute("id")%>).length)
	    		$( "#packet-" + <%=request.getAttribute("id") %>).click();
	    });
	    
	    </script>
	    
	   <%} %>
	
</body>
</html>