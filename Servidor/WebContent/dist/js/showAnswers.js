var lastQuestionClicked = -1;

$(document).ready(function() {
	$("#div-answers").hide();
			
	  $("#preguntas").delegate('tbody tr','click', function() {
		  var idp = $(this).attr("id");
		  idp -= 0;
	  
	  if (idp != lastQuestionClicked){
		  
		  //Colorido de fila clickeada
		  $.each($("#preguntas tbody tr"), function() {
				$(this).removeClass("rowselected");
		  });
		  $(this).addClass("rowselected");
		  //Fin colorido
		  
		  $("#bodyresp").empty()
		  var cantAnswers = $("#question"+idp+"answers").val();
		  for(i=0;i<cantAnswers;i++){
			  var fila = "<tr><td>";
			  	var answer = $("#question"+idp+"answer"+i).val();
			  fila+= answer; 
			  fila += "</td><td>" + $("#question"+idp+"answerCategories"+i).val(); 
			  fila+="</td></tr>";
			  $("#bodyresp").append(fila);
		  }
		  lastPacketClicked = idp;
		  $("#div-answers").show();
	  }
	});
	 $(document).on('click',"[name^='button-delete-question-']",function() {
		 var attributoIdQuestion = ($(this).attr("name")).split("-")[3];
		 deleteQuestion(attributoIdQuestion);
	  });
	 
});

function deleteQuestion(questionId)
{ 
	var divMessageInfoServer = "<div style=\"margin-top: 8px;\"><span id=\"packetDelete-info-text\"style=\"display: inline;; padding-right:20px; font-size: medium;\">Deleting...</span></div>";
	
	$.ajaxSetup({
		beforeSend : function() {
			$('#button-delete-question-' + questionId).parent().append(divMessageInfoServer);
			$('#button-delete-question-' + questionId).remove();
			$('#packetDelete-info-text').addClass("working-indicator2");
			$('#packetDelete-info-text').addClass("text-info");
		}
	});
	$.ajax({
        url: 'QuestionList',
        data: {
            query: 'questionDelete',
            id: questionId
        },
        success: function(response) {
        	$("#div-answers").empty();
        	$("#div-answers").hide();
        	       
			$.each($("#preguntas tbody tr"), function() {
					 var elementId = $(this).attr("id");
					if (elementId == questionId)
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