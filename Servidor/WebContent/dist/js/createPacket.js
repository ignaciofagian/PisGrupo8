var packetStocks = {};
var packetName = "";
var packetType = "";
var packetCategories = [];
var packetQuestions = {};
var packetAlgorithm = "";

var wasSelectedItemFromAutocomplete = false;
var isDuplicatedItemFromAutocomplete = false;

$(document).ready(function() {

	receiveDataCategoriesFromServer();

	$('#dropdown-packet-categories').on('change', function() {
		$(this).valid();
	});

	$('#dropdown-packet-type').multiselect({

		buttonWidth : '100%'
	});

	var navListItems = $('ul.setup-panel li a'), allWells = $('.setup-content'), wizzardPanel = $('#packet-wizzard')
	questionId = 0, answerId = 0, stockId = 0;
	// Algoritmo 1
	// indice 0

	navListItems.click(function(e) {
		e.preventDefault();
		var $target = $($(this).attr('href')), $item = $(this).closest('li');
		
		
		
		if (!$item.hasClass('disabled')) {
			navListItems.closest('li').removeClass('active');
			$item.addClass('active');
			allWells.hide();
			$target.show();
		}
		
		var h = $('a', $item).attr('href');
		if (h == '#step-3'){
			var editor = ace.edit("algorithm-examples");
			editor.getSession().setMode("ace/mode/plainStock");
			editor.setTheme("ace/theme/eclipse");
			editor.$blockScrolling = Infinity
			editor.getSession().setTabSize(2);
			editor.getSession().setUseWrapMode(true);
			editor.setReadOnly(true);
			editor.setOptions({
			  fontSize: "16px",
			 
			});
			
			var examples = "//Example1\n" +
					"VAR Float x ENDVAR\n" +
					"x = HISTORY[-1]\n" +
					"if x > 100 then\n" +
					"\tBUY(50.5)\n" +
					"else\n" +
					"\tSELL(50.5)\n" +
					"endif" +
					"\n\n//Example2\n" +
					"VAR Float x ENDVAR\n" +
					"x = avg[-4, 0]\n" +
					"if x > 100 then\n" +
					"\tBUY(50.5)\n" +
					"endif" + 
					"\n\n//Example3\n" +
					"VAR Float x ENDVAR\n" +
					"x = dev[-4, 0]\n" +
					"if x > 100 then\n" +
					"\tBUY(50.5)\n" +
					"endif" + 
					"\n\n//Example4\n" +
					"VAR Float a,b,c,d \n" +
					"Float e,f ENDVAR\n" +
					"a = 10 + 5\n" +
					
					"b = 10 - 5\n" +
					
					"c = 10 * 5\n" +
					
					"d = 10 / 5\n" +
					
					"e = 10 mod 5\n" +
					
					"f = 10 ^ 5\n" +
					"if a > 100 then\n" +
					"\tBUY(50.5)\n" +
					"endif" +
					"\n\n//Example5\n" +
					"VAR Boolean a,b,c,d \n" +
					"Float e,f ENDVAR\n" +
					"a = true\n" +
					
					"b = false\n" +
					
					"c = (1>2) and (a or b)\n" +
					
					"d = not b\n" +
					
					"e = 1 + 2 * (4*(5+2))\n" +
					
					"f = 10 ^ 5\n" +
					"if c then\n" +
					"\tBUY(50.5)\n" +
					"endif";
			editor.setValue(examples, -1);
			
			
			$('#examples-header').show();
			$('#algorithm-examples').show();
		}else{
			$('#examples-header').hide();
			$('#algorithm-examples').hide();
		}
	});

	$('ul.setup-panel li.active a').trigger('click');

	$('#confirm-packet-details').on('click',function(e) {

				if (!$('#panelPacketDetails').valid()) {
					return false;
				}

				if ($('#dropdown-packet-type option:selected').val() == 1) {
					packetType = 1;

					$('ul.setup-panel li:eq(2)').removeClass('hidden');
					$('#go-next-from-step-2').text('Next');

				} else {
					packetType = 0;
					$('ul.setup-panel li:eq(2)').addClass('hidden');
					$('#go-next-from-step-2').text('Confirm');
				}

				packetName = $("[name='packetName']").val().trim();

				$('#dropdown-packet-categories :selected').each(
						function(i, selected) {
							packetCategories[i] = $(
									selected).val();
						});

				wizzardPanel.show();
			});

	$('#go-next-from-step-1').on('click',function(e) {
				$('ul.setup-panel li:eq(1)').removeClass('disabled');
				$('ul.setup-panel li a[href="#step-2"]').trigger('click');
			});

	$('#go-next-from-step-2').on('click',function(e) {
				if (packetType == 0) {
					// Confirmacion paquete indice
					checkConfirmPacket();

				} else {
					$('ul.setup-panel li:eq(2)').removeClass('disabled');
					$('ul.setup-panel li a[href="#step-3"]').trigger('click');

				}
			});

	$('#go-back-from-step-2').on('click',function(e) {
				$('ul.setup-panel li a[href="#step-1"]').trigger('click');
			});

	$('#go-next-from-step-3').on('click', function(e) {
		// Confirmacion paquete algoritmico
		if (packetAlgorithm == editor.getValue()){
			packetAlgorithm = editor.getValue();
			checkConfirmPacket();
		}else{
			//if they are not the same, we need to test the algorithm first
			BootstrapDialog.show({
				type : BootstrapDialog.TYPE_DANGER,
				title : 'Algorithm interpretation',
				message :'Please test your algorithm first. Click on Testing',
				buttons : [ {
					label : 'Ok',
					action : function(dialog) {
						dialog.close();
						return false;
					}
				} ]
			});
		}
		
	});

	$('#go-back-from-step-3').on('click',function(e) {
				$('ul.setup-panel li a[href="#step-2"]').trigger('click');
			});

	//$('#show-errors').on('click', function(e) {
	//	$('#algorithm-code-editor').height(250);
	//	$('#algorithm-errors').removeClass('hidden');
	//});

	$('#btn-create-question').on('click',function(e) {
				
				
				if (!$('#panelSpecificQuestions').valid()) {
					return false;
				}
				$('.panel-collapse.in').collapse('hide');

				var questionTextEng = $("#input-question-text-eng").val();
				var questionTextEsp = $("#input-question-text-esp").val();

				var question = {
						questionTextEng : questionTextEng,
						questionTextEsp : questionTextEsp,
						answers : {}
				};
				packetQuestions[questionId] = question;

				$("#input-question-text-eng").val("");
				$("#input-question-text-esp").val("");

				var html = "";
				html += "<div class=\"panel panel-default\">";
				html += "   <div class=\"panel-heading\">";
				html += "      <div class=\"row\">";
				html += "         <div class=\"col-xs-10\">";
				html += "            <h4 class=\"panel-title\" style=\"margin-top:10px;\">";
				html += "               <a data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#question"
					+ questionId
					+ "\" aria-expanded=\"true\">Question: "
					+ questionTextEng
					+ " | "
					+ questionTextEsp + "<\/a>";
				html += "            <\/h4>";
				html += "         <\/div>";
				html += "         <div class=\"col-xs-2\">";
				html += "            <button name=\"btn-delete-question-"
					+ questionId
					+ "\" type=\"button\" style=\"float:right;\" class=\"btn btn-danger btn-sm\"><span class=\"glyphicon glyphicon-trash\"><\/span>Delete<\/button>";
				html += "         <\/div>";
				html += "      <\/div>";
				html += "   <\/div>";
				html += "   <div id=\"question"
					+ questionId
					+ "\" class=\"panel-collapse collapse in\" aria-expanded=\"true\">";
				html += "      <div class=\"panel-body\">";
				html += "         <div class=\"row\">";
				html += "            <div class=\"col-md-4\">";
				html += "               <div class=\"panel panel-info text-center\">";
				html += "                  <div class=\"panel-heading\">";
				html += "                     <span class=\"left-side \">New answer<\/span>";
				html += "                  <\/div>";
				html += "            <form  role=\"form\" name=\"panelNewAnswer\">";
				html += "                  <div class=\"panel-body\">";
				html += "                     <div class=\"form-group text-left\">";
				html += "                        <label>Answer ENG<\/label>";
				html += "                        <textarea class=\"form-control\" name=\"answerEng\" style=\"resize:vertical;\" rows=\"2\"><\/textarea>";
				html += "                     <\/div>";
				html += "                     <div class=\"form-group text-left\">";
				html += "                        <label>Answer ESP<\/label>";
				html += "                        <textarea class=\"form-control\" name=\"answerEsp\" style=\"resize:vertical;\" rows=\"2\"><\/textarea>";
				html += "                     <\/div>";
				html += "                     <div class=\"form-group text-left\">";
				html += "                        <label>Points [0..10]<\/label>";
				html += "                        <input name=\"answerPoints\" class=\"form-control\">";
				html += "                     <\/div>";
				html += "                     <button type=\"button\" id=\"btn-create-answer-"
					+ questionId
					+ "\" name=\"btn-add-answer\" class=\"btn btn-info pull-right\">Add<\/button>";
				html += "                  <\/div>";
				html += "<\/form>";
				html += "               <\/div>";
				html += "            <\/div>";
				html += "            <div class=\"col-md-8\">";
				html += "               <div class=\"panel panel-info\">";
				html += "                  <div class=\"panel-heading\">";
				html += "                     <span class=\"left-side\"> Answer list<\/span>";
				html += "                  <\/div>";
				html += "                  <div class=\"panel-body\">";
				html += "                     <div class=\"table-responsive\" style=\"height: 272px;\">";
				html += "                        <table  class=\"table table-hover\">";
				html += "                           <thead>";
				html += "                              <tr>";
				html += "                                 <th>Answer<\/th>";
				html += "                                 <th>Points<\/th>";
				html += "                                 <th>Delete<\/th>";
				html += "                              <\/tr>";
				html += "                           <\/thead>";
				html += "                           <tbody>                     ";
				html += "                           <\/tbody>";
				html += "                        <\/table>";
				html += "                     <\/div>";
				html += "                  <\/div>";
				html += "               <\/div>";
				html += "            <\/div>";
				html += "         <\/div>";
				html += "      <\/div>";
				html += "   <\/div>";
				html += "<\/div>";
				html += "";

				var idac = "#question" + questionId;
				$('#accordion').append(html);

				questionId++;
			});

	$(document).on('click',	"[name^='btn-delete-question']",function() {

				$(this).parent('div').parent('div')
				.parent('div').parent('div')
				.andSelf().remove();

				var attributoIdQuestion = ($(this)
						.attr("name")).split("-")[3];

				delete packetQuestions[attributoIdQuestion];

				return false;
			});

	$(document).on(	'click',"[name='btn-add-answer']",	function() {
		var inputAnswerEng = $(this).prev()
		.prev().prev().find("textarea");
		
		var inputAnswerEsp = $(this).prev()
		.prev().find("textarea");
		
		inputAnswerEng.val(inputAnswerEng.val().trim());
		inputAnswerEsp.val(inputAnswerEsp.val().trim());
		
		if (!$(this).parent().parent().valid()) {
					return false;
				}

			
				
				var inputPoints = $(this).prev().find(
				"input");
				
				var tableAnswers = $(this)
				.parent('div').parent('form')
				.parent('div').parent('div')
				.parent('div').find('table')
				.find('tbody');

				var answer = {
						answerEng : inputAnswerEng.val(),
						answerEsp : inputAnswerEsp.val(),
						points : inputPoints.val()
				};

				var attributoIdPregunta = ($(this)
						.attr("id")).split("-")[3];
				packetQuestions[attributoIdPregunta].answers[answerId] = answer;

				var newRowHtml = "<tr>";
				newRowHtml += "<td>" + "ENG: "
				+ inputAnswerEng.val() + "<br>"
				+ "SPA: "
				+ inputAnswerEsp.val()
				+ "</td>";
				newRowHtml += "<td>"
					+ inputPoints.val() + "</td>";
				newRowHtml += "<td>"
					+ "<button type=\"button\" name=\"btn-delete-answer-"
					+ answerId
					+ "\"  class=\"btn btn-danger btn-sm\"><span class=\"glyphicon glyphicon-trash\"><\/span> Delete<\/button>"
					+ "</td>";
				newRowHtml += "</tr>";
				tableAnswers.append(newRowHtml);

				inputAnswerEng.val("");
				inputAnswerEsp.val("");
				inputPoints.val("");

				answerId++;

				return false;
			});

	$(document).on(	'click',"[name^='btn-delete-answer']",	function() {
				$(this).parent().parent().andSelf()
				.remove();
				var attributoIdAnswer = ($(this)
						.attr("name")).split("-")[3];

				for ( var i in packetQuestions) {
					Object
					.keys(
							packetQuestions[i].answers)
							.forEach(
									function(key) {
										if (key == attributoIdAnswer) {
											delete packetQuestions[i].answers[key];
											return;
										}
									});
				}

			});

	$('#btn-add-stock').on('click',	function(e) {

				if (!$('#panelStocks').valid()) {
					return false;
				}

				var valInputPorcentage = $(this).prev().find("input").val();
				var valInputStock = $(this).prev().prev().find("input").val();

				var stockSymbol = valInputStock.substr(
						0, valInputStock.indexOf(' '));
				var StockName = valInputStock
				.substr(valInputStock
						.indexOf(' '));

				$(this).prev().find("input").val("");
				$(this).prev().prev().find("input")
				.val("");

				var stock = {
						symbol : stockSymbol,
						name : StockName,
						porcentage : valInputPorcentage
				};

				packetStocks[stockId] = stock;

				var tableStocks = $(this)
				.parent('form').parent('div')
				.parent('div').parent('div')
				.parent('div').find('table')
				.find('tbody');

				var newRowHtml = "<tr>";
				newRowHtml += "<td>" + stockSymbol
				+ "</td>";
				newRowHtml += "<td>" + StockName
				+ "</td>";
				newRowHtml += "<td>"
					+ valInputPorcentage + "%"
					+ "</td>";
				newRowHtml += "<td>"
					+ "<button type=\"button\" name=\"btn-delete-stock-"
					+ stockId
					+ "\"  class=\"btn btn-danger btn-sm\"><span class=\"glyphicon glyphicon-trash\"><\/span> Delete<\/button>"
					+ "</td>";
				newRowHtml += "</tr>";
				tableStocks.append(newRowHtml);

				stockId++;
			});

	$(document).on(	'click',"[name^='btn-delete-stock']",function() {
				$(this).parent().parent().andSelf().remove();

				var attributoIdStock = ($(this).attr("name"))
				.split("-")[3];

				delete packetStocks[attributoIdStock];

			});

});

function checkConfirmPacket() {
	var totalPorcentageStocks = getTotalStockPorcentages();
	// Verifico que en la lista de stocks hay alguno y ademas completan el 100%
	if (Object.keys(packetStocks).length == 0) {
		message = "Step 1: Must add at least one stock to packet";

	} else if (totalPorcentageStocks != 100) {
		message = "Step 1: Total stocks percentage is: ";
		message += totalPorcentageStocks + "% ";
		message += ", must be 100%.";
	} else if (Object.keys(packetQuestions).length == 0) {
		message = "Step 2: Must add at least one specific question";
	} else if (!checkAllQuestionHaveAnAnswer()) {
		message = "Step 2: All question must have at least two answers";
	} else {
		sendDataPacketToServer();
		return;
	}

	BootstrapDialog.show({
		type : BootstrapDialog.TYPE_DANGER,
		title : 'Error on create packet',
		message : message,
		buttons : [ {
			label : 'Ok',
			action : function(dialog) {
				dialog.close();
				return false;
			}
		} ]
	});
}

function checkAllQuestionHaveAnAnswer() {
	for ( var i in packetQuestions) {
		if (Object.keys(packetQuestions[i].answers).length == 0)
			return false;
	}

	return true;
}

function receiveDataCategoriesFromServer() {
	$.ajax({
		url : 'CreatePacket',
		data : {
			query : 'categories'
		},
		success : function(response) {
			$.each(response, function(index, value) {
				$('#dropdown-packet-categories').append(
						"<option value=\"" + value.id + "\">" + value.name
						+ "</option>");
			});

			$('#dropdown-packet-categories').multiselect({
				buttonWidth : '100%'
			});

		},
	});
}

function sendDataPacketToServer() {
	var packetData = {
			name : packetName,
			type : packetType,
			categories : packetCategories,
			stocks : packetStocks,
			questions : packetQuestions,
			algorithm : packetAlgorithm
	}

	var jsonStr = JSON.stringify(packetData);

	$.ajaxSetup({
		beforeSend : function() {
			$.blockUI({
				message : "Wait packet creation ...",
				css : {
					border : 'none',
					padding : '15px',
					backgroundColor : '#000',
					'-webkit-border-radius' : '10px',
					'-moz-border-radius' : '10px',
					opacity : .5,
					color : '#fff'
				}
			});
		},
		complete : function() {
			$.unblockUI;
		}
	});
	$.ajax({
		url : 'CreatePacket',
		type : 'post',
		dataType : 'json',
		data : jsonStr,
		error : function() {
			$.unblockUI();
			BootstrapDialog.show({
				type : BootstrapDialog.TYPE_DANGER,
				title : 'Packet',
				message : 'Error creating packet',
				buttons : [ {
					label : 'Ok',
					action : function(dialog) {
						dialog.close();
						return false;
					}
				} ]
			});
		},
		success : function(data) {
			$.unblockUI();
			BootstrapDialog.show({
				type : BootstrapDialog.TYPE_SUCCESS,
				title : 'Packet',
				message : 'Packet created!',
				buttons : [ {
					label : 'Ok',
					action : function(dialog) {
						window.location = 'index.jsp';
						dialog.close();
						return false;
					}
				} ]
			});
		},
		complete: function(){
			$.unblockUI();
		   }
	});
}
