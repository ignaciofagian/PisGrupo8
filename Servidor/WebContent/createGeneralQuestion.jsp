<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>

    <head>
        <jsp:include page="include/header.jsp" />
        <link href="dist/libs/bootstrap/dist/css/bootstrap-dialog.min.css" rel="stylesheet">
    </head>

    <body>
        <div id="wrapper">
            <jsp:include page="include/navigationBar.jsp" />
            <div id="page-wrapper">
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">Create new general question</h1>
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
                <!-- /.row -->
               <div class="row">
                <div class="col-md-12">
                    <form class="text-left" role="form" name="panelNewQuestion">
                        <div class="form-group">
                            <label>question</label>
                            <input id="input-question-text-eng" name="questionEng" class="form-control" placeholder="English question" />
                            <input id="input-question-text-esp" name="questionEsp" class="form-control" placeholder="Spanish question" style="margin-top: 10px;" />
                        </div>
                    </form>
                    <div class="row">
                        <div class="col-md-3">
                            <div class="panel panel-info text-center">
                                <div class="panel-heading">
                                    <span class="left-side ">New answer</span>
                                </div>
                                <form role="form" name="panelNewAnswer">
                                    <div class="panel-body">
                                        <div class="form-group text-left">
                                            <label>Answer ENG</label>
                                            <textarea class="form-control" name="answerEng" style="resize: vertical;" rows="2"></textarea>
                                        </div>
                                        <div class="form-group text-left">
                                            <label>Answer ESP</label>
                                            <textarea class="form-control" name="answerEsp" style="resize: vertical;" rows="2"></textarea>
                                        </div>
                                        <div class="form-group text-left">
                                            <label>Category</label>
                                            <select id="dropdown-packet-categories" name="packetCategories" multiple="multiple">
                                            </select>
                                        </div>
                                        <button type="button" name="btn-add-answer" id="btn-add-answer" class="btn btn-info pull-right">Add</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="col-md-9">
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <span class="left-side"> Answer list</span>
                                </div>
                                <div class="panel-body">
                                    <div class="table-responsive" style="height: 272px;">
                                        <table class="table table-hover">
                                            <thead>
                                                <tr>
                                                    <th>Answer</th>
                                                    <th>Category</th>
                                                    <th>Delete</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row pull-right" style="margin-bottom: 10px;">
                        <button id="btn-cancel" type="button" class="btn btn-primary">Cancel</button>
                        <button id="btn-save" type="button" class="btn btn-primary margin-right">Save</button>
                    </div>
                </div>
            </div>
             </div>
        </div>
        <jsp:include page="include/footer.jsp" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.5/js/bootstrap-dialog.min.js"></script>
        <script src="dist/libs/jquery/dist/jquery.validate.min.js"></script>
        <script src="dist/libs/blockUI/jquery.blockUI.js"></script>
        <script src="dist/js/createGeneralQuestion.js"></script>
    </body>

    </html>
