var lastPacketClicked = -1;
$(document).ready(function() {
	getPacketList();
	
	$("#table-packet-list").delegate('tbody tr', 'click', function() {
		var actualId =$(this).attr("id").split("-")[1];
		
		if (lastPacketClicked != actualId)
		{
			$.each($("#table-packet-list tbody tr"), function() {
					$(this).removeClass("rowselected");
			});
			
			$(this).addClass("rowselected");
						
			lastPacketClicked = actualId;
			getPacketDetail(actualId, $(this));
				
		}
    });
	
	$(document).on(	'click',"[name^='button-delete-packet-']",	function() {
		//$(this).parent().parent().andSelf().remove();
		
		var attributoIdPacket = ($(this).attr("name")).split("-")[3];
		
		deletePacket(attributoIdPacket);
	});
});

function getPacketList()
{
	$.ajax({
        url: 'PacketList',
        data: {
            query: 'packetList'
        },
        success: function(response) {
            $.each(response, function(index, value) {
                $('#table-packet-list').find('tbody').append(
                		"<tr id=\"packet-" + value.id +  "\"" + "><td>" +  value.nombre  +"</td></tr>")
            });
        },
        async :false
    });
}

function getPacketDetail(packetId, rowElement)
{
	var packetStocks = [];
	var packetName = "";
	var packetType = 0;
	var packetCategories = [];
	var packetQuestions = [];
	var packetAlgorithm = "";
	$.ajaxSetup({
		beforeSend : function() {
			rowElement.addClass("working-indicator");
		}
	});
	$.ajax({
        url: 'PacketList',
        data: {
            query: 'packetDetail',
            id: packetId
        },
        success: function(response) {
        	rowElement.removeClass("working-indicator");
        	packetName = response.nombre;
        	packetType = response.tipo;
        	packetCategories = response.categorias;
        	packetQuestions = response.preguntas;
        	packetStocks = response.acciones;
        	
        	if (packetType == 1)
        		packetAlgorithm = response.algoritmo;
        	
        	populatePacketView(packetId,packetStocks, packetName, packetType, packetCategories, packetQuestions, packetAlgorithm)
        }
    });
}

function populatePacketView(packetId,packetStocks, packetName, packetType,
	 packetCategories, packetQuestions, packetAlgorithm )
{
	if (!$('#packet-detail').is(":visible"))
		$('#packet-detail').show()
		
	var html = "";
	html += "<div class=\"row\">";
	html += "<div class=\"col-lg-10\">";
	html += "<h4 class=\"text-center\">"+ packetName + "</h4>";
	html += "</div>";
	html += "<div class=\"col-lg-2\">";
	html += "<button name=\"button-delete-packet-"+ packetId + "\" id=\"button-delete-packet-"+ packetId + "\"  class=\"btn btn-danger btn-sm pull-right\" ><span class=\"glyphicon glyphicon-trash\"><\/span> Delete<\/button>";
	html += "</div>";
	html += "</div>";
	html += "<div class=\"panel-group\" id=\"accordion\">";
	
	var htmlQuestions = "";
	var htmlStocks="";
	var htmlCategories = "";
	var htmlAlgorithm="";
	
	var i = 0;
	packetQuestions.forEach(function(question) {
	    
	    htmlQuestions = "<div class=\"panel panel-default\">";
	    htmlQuestions += "	<div class=\"panel-heading\">";
	    htmlQuestions += "		<div class=\"row\">";
	    htmlQuestions += "			<div class=\"col-xs-12\">";
	    htmlQuestions += "	      	<h4 class=\"panel-title\" style=\"margin-top:10px;\">";
	    htmlQuestions += "	          	<a data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#question"+ i +"\"  aria-expanded=\"true\">Question " + i +"</a>";
	    htmlQuestions += "	          </h4>";
	    htmlQuestions += "	      </div>";
	    htmlQuestions += "	  </div>";
	    htmlQuestions += "	</div>";
	    
	    htmlQuestions += "	<div id=\"question" + i + "\" class=\"panel-collapse collapse in\" aria-expanded=\"true\">";
	    htmlQuestions += "		<div class=\"panel-body\">";
	    htmlQuestions += "			<h3>" + question.eng + "</h3> ";
	    htmlQuestions += "			<h3>" + question.esp + "</h3> ";
	    htmlQuestions += "			<div class=\"table-responsive\" style=\"height: 180px;\">";
	    htmlQuestions += "				<table class=\"table\">";
	    htmlQuestions += "					<thead>";
	    htmlQuestions += "						<tr>";
	    htmlQuestions += "							<th>Answer</th>";
	    htmlQuestions += "							<th>Points</th>";
	    htmlQuestions += "						</tr>";
	    htmlQuestions += "					</thead>";
	    htmlQuestions += "					<tbody>";
	    
	    question.respuestas.forEach(function(answer) {
	    	htmlQuestions += "					<tr>";
	    	htmlQuestions += "						<td>" + "ENG: " + answer.eng + "<br>" + "SPA: " + answer.esp + "</td>";
	    	htmlQuestions += "						<td>" + answer.puntaje + "</td>";
		    htmlQuestions += "					</tr>";
	    });  
	    
	    htmlQuestions += "					</tbody>";
	    htmlQuestions += "				</table>";
	    htmlQuestions += "			</div>";
	    htmlQuestions += "		</div>";
	    htmlQuestions += "	</div>";
	    htmlQuestions += "</div>";
	    i++;

	    html += htmlQuestions;
	});
	html += "</div>";

	$('#packet-detail').empty();
	
	$('#packet-detail').append(html);
	
	htmlStocks += "<div class=\"row\">";
	htmlStocks += "	<div class=\"col-md-9\">";
	htmlStocks += "		<div class=\"panel panel-default\">";
	htmlStocks += "			<div class=\"panel-heading text-center\">";
	htmlStocks += "				Stock list";
	htmlStocks += "			</div>";
	htmlStocks += "			<div class=\"panel-body\">";
	htmlStocks += "				<div class=\"table-responsive\" style=\"height: 200px;\">";
	htmlStocks += "					<table class=\"table\">";
	htmlStocks += "						<thead>";
	htmlStocks += "							<tr>";
	htmlStocks += "								<th>Symbol</th>";
	htmlStocks += "								<th>Name</th>";
	htmlStocks += "								<th>Porcentage</th>";
	htmlStocks += "							</tr>";
	htmlStocks += "						</thead>";
	htmlStocks += "						<tbody>";
	
	packetStocks.forEach(function(stock) {
		htmlStocks += "							<tr>";
		htmlStocks += "								<th>" + stock.simbolo + "</th>";
		htmlStocks += "								<th>" + stock.nombre + "</th>";
		htmlStocks += "								<th>" + stock.porcentaje + "</th>";
		htmlStocks += "							</tr>";
	});
	
	htmlStocks += "						</tbody>";
	htmlStocks += "					</table>";
	htmlStocks += "				</div>";
	htmlStocks += "			</div>";
	htmlStocks += "		</div>";
	htmlStocks += "	</div>";
	
	htmlCategories += " <div class=\"col-md-3\">";
	htmlCategories += "	<div class=\"panel panel-default\">";
	htmlCategories += "		<div class=\"panel-heading text-center\">";
	htmlCategories += "			Packet categories";
	htmlCategories += "		</div>";
	htmlCategories += "		<div class=\"panel-body\">";
	htmlCategories += "			<div class=\"table-responsive\">";
	htmlCategories += "				<table class=\"table\">";
	htmlCategories += "					<thead>";
	htmlCategories += "						<tr>";
	htmlCategories += "							<th>Category</th>";
	htmlCategories += "						</tr>";
	htmlCategories += "					</thead>";
	htmlCategories += "					<tbody>";
	
	packetCategories.forEach(function(category) {
		htmlCategories += "							<tr>";
		htmlCategories += "								<th>" + category.name + "</th>";
		htmlCategories += "							</tr>";
	});
	
	htmlCategories += "					</tbody>";
	htmlCategories += "				</table>";
	htmlCategories += "			</div>";
	htmlCategories += "		</div>";
	htmlCategories += "	 </div>";
	htmlCategories += " </div>";
	htmlCategories += "</div>";
	

	$('#packet-detail').append(htmlStocks + htmlCategories);
	
	if (packetType == 1)
	{
		htmlAlgorithm += " <div class=\"row\">";
		htmlAlgorithm += "	<div class=\"coll-lg-12\" style=\"margin-left:15px; margin-right:15px;\">";
		htmlAlgorithm += "		<div class=\"panel panel-primary\">";
		htmlAlgorithm += "			<div class=\"panel-heading text-center\">";
		htmlAlgorithm += "				Algorithm";
		htmlAlgorithm += "			</div>";
		htmlAlgorithm += "			<div class=\"panel-body\">";
		htmlAlgorithm += "				<div id=\"algorithm-code-editor\">";
		htmlAlgorithm += "				</div>";
		htmlAlgorithm += "			</div>";
		htmlAlgorithm += "		</div>";
		htmlAlgorithm += "";
		htmlAlgorithm += "	</div>";
		htmlAlgorithm += "</div>";
		
		$('#packet-detail').append(htmlAlgorithm);
		
		var editor = ace.edit("algorithm-code-editor");
		editor.getSession().setMode("ace/mode/plainStock");
		editor.setTheme("ace/theme/eclipse");
		editor.$blockScrolling = Infinity
		editor.getSession().setTabSize(2);
		editor.getSession().setUseWrapMode(true);
		editor.setReadOnly(true);
		editor.setOptions({
		  fontSize: "16px",
		 
		});
		
		editor.setValue(packetAlgorithm, -1);
		
	}
	
	
}

function deletePacket(packetId)
{ 
	var divMessageInfoServer = "<div style=\"margin-top: 8px;\"><span id=\"packetDelete-info-text\"style=\"display: inline;; padding-right:20px; font-size: medium;\">Deleting...</span></div>";
	
	$.ajaxSetup({
		beforeSend : function() {
			$('#button-delete-packet-' + packetId ).parent().append(divMessageInfoServer);
			$('#button-delete-packet-' + packetId ).remove();
			$('#packetDelete-info-text').addClass("working-indicator2");
			$('#packetDelete-info-text').addClass("text-info");
		}
	});
	$.ajax({
        url: 'PacketList',
        data: {
            query: 'packetDelete',
            id: packetId
        },
        success: function(response) {
        	
        	$('#packet-detail').empty();
        	$('#packet-detail').hide();
       
			$.each($("#table-packet-list tbody tr"), function() {
					var elementId = $(this).attr("id").split("-")[1];
					
					if (elementId == lastPacketClicked)
					{
						$(this).remove();
					}
			});   			
        },
        error : function() {
        	$('#packetDelete-info-text').removeClass("working-indicator2");
        	$('#packetDelete-info-text').removeClass("text-info");
        	$('#packetDelete-info-text').addClass("text-danger");
        	$('#packetDelete-info-text').text("Error");
        },
        async :false
    });
}