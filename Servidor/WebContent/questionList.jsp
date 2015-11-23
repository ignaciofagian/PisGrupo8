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
                    <h1 class="page-header">General Questions</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            General question list
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="table-responsive">
                            <form method="post" id="DeleteForm">
                                <table id="preguntas" class="table table-bordered table-hover" >
                                    <thead>
                                        <tr>
                                            <th>Number</th>
                                            <th>Question</th>
                                            <th>Delete</th>
                                        </tr>
                                    </thead>
                                    <tbody>
										<%
										int largo = Integer.parseInt(request.getAttribute("questionlength").toString());
										if(largo!=0){
											int it = 0;
											while(it<largo){
												%>
												<tr id="<%=request.getAttribute("questionid"+it).toString() %>">
												<td><%out.print(request.getAttribute("questionid"+it).toString()); %></td>
												<td><%out.print(request.getAttribute("questioneng"+it).toString());%>
												&nbsp;&nbsp;|&nbsp;&nbsp;
												<%out.print(request.getAttribute("questionesp"+it).toString());%>
												</td>
												<td>
												
												<button  id="button-delete-question-<%= request.getAttribute("questionid"+it).toString()%>" name="button-delete-question-<%= request.getAttribute("questionid"+it).toString()%>" class="btn btn-danger btn-sm" value="<%= request.getAttribute("questionid"+it).toString()%>">
												<span class="glyphicon glyphicon-trash"></span> 
												Delete</button>
												
												</td>
												 </tr>
												 <%
												 it++;
											}
										}
										%>
                                    </tbody>
                                </table>
                             
                                </form>
                                
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <%for(int j=0;j<largo;j++){ %>
            	<%int resp = Integer.parseInt(request.getAttribute("question"+j+"answers").toString());
            	%><input type="hidden" id="question<%=j%>answers" value="<%=resp%>" />
            <%for(int k=0;k<resp;k++){%>
            <input type="hidden" id="question<%=j%>answer<%=k%>" value="<%=request.getAttribute("question"+j+"answer"+k).toString()%>" />	
			<input type="hidden" id="question<%=j%>answerCategories<%=k%>" value="<%=request.getAttribute("question"+j+"answerCategories"+k).toString()%>" />
			<%} %>
			<%} %>
            
            <div id="div-answers" class="row" hidden>
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table id="respuestas" class="table">
                                    <thead>
                                        <tr>
                                            <th>Answer</th>
                                             <th>Categories</th>
                                        </tr>
                                    </thead>
                                    <tbody id="bodyresp">
                                        <tr>
                                            <td>This is the first answer?</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
   
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->
    </div>
    <jsp:include page="include/footer.jsp" />
    <script src="dist/libs/ace/ace.js"></script>
	<script src="dist/libs/jquery/dist/jquery.validate.min.js"></script>
    <script src="dist/js/showAnswers.js"></script>
</body>
</html>