var questionTextEng = "";
var questionTextEsp = "";
var questionAnswers = {};

$(document).ready(function() {
	
	var answerId = 0;
	
	receiveDataCategoriesFromServer();
	
	$.validator.addMethod("needsSelection", function(value, element) {
        var count = $(element).find('option:selected').length;
        return count > 0;
    });
	
	$('#dropdown-packet-categories').on('change', function() {
        $(this).valid();
    });
	
	$("[name='panelNewQuestion']").validate({
		 rules: {
			 questionEng: {
	                minlength: 5,
	                required: true
	            },
	            questionEsp: {
	                minlength: 5,
	                required: true
	            }
	        }
	 });
	
	 $("[name='panelNewAnswer']").validate({
    	 ignore: [],
        rules: {
            answerEng: {
                minlength: 5,
                required: true
            },
            answerEsp: {
                minlength: 5,
                required: true
            },
            packetCategories: {
                needsSelection: true
            }
        },
        messages: {
        	  packetCategories: {
                  needsSelection: "Select at least one category"

              }
            
        },
        errorPlacement: function(error, element) {
            if (element.attr("name") == "packetCategories") {
                element.next().append(error);
            } else {

                error.insertAfter(element);
            }
        }
       
    });

	
	$('#btn-add-answer').on('click',function(e) {
			var inputAnswerTextEsp = $(this).prev().prev().find('textarea'); //esp
	        var inputAnswerTextEng = $(this).prev().prev().prev().find('textarea'); //eng
			
	        inputAnswerTextEsp.val(inputAnswerTextEsp.val().trim());
	        inputAnswerTextEng.val(inputAnswerTextEng.val().trim());
			
			if (!$(this).parent().parent().valid()) {
	            return false;
	        }
		
	        var answerCategories = [];
	        var answerCategoriesText = [];
	        
	        $('#dropdown-packet-categories :selected').each(function(i, selected) {
                	answerCategories[i] = $(selected).val();
                	answerCategoriesText[i] = $(selected).text();
            });
	        
	        var tableAnswers =  $(this).parent().parent().parent().parent().parent().find('table').find('tbody');
	       
	        var answer = {
                    answerEng: inputAnswerTextEng.val(),
                    answerEsp: inputAnswerTextEsp.val(),
                    categories : answerCategories
                };
	        
	        questionAnswers[answerId] = answer;

	        var newRowHtml = "<tr>";
            newRowHtml += "<td>" + "ENG: " + answer.answerEng + "<br>" + "SPA: " + answer.answerEsp + "</td>";
            newRowHtml += "<td>" +  answerCategoriesText + "</td>";
            newRowHtml += "<td>" + "<button type=\"button\" name=\"btn-delete-answer-" + answerId + "\"  class=\"btn btn-danger btn-sm\"><span class=\"glyphicon glyphicon-trash\"><\/span> Delete<\/button>" + "</td>";
            newRowHtml += "</tr>";
            tableAnswers.append(newRowHtml);
	       
            inputAnswerTextEsp.val("");
            inputAnswerTextEng.val("");
          
            $("#dropdown-packet-categories").multiselect("clearSelection");
            $("#dropdown-packet-categories").multiselect( 'refresh' );
           
            
	        answerId++;
	});
	
	$('#btn-cancel').on('click',function(e) {
		window.location = 'index.jsp';
	});
	
	$('#btn-save').on('click',function(e) {

		if (!$("[name='panelNewQuestion']").valid())
			return;
		
		questionTextEsp = $('#input-question-text-esp').val();
		questionTextEng = $('#input-question-text-eng').val();
		
		checkConfirmQuestion();
	});
	
	$(document).on('click', "[name^='btn-delete-answer']", function() {
            $(this).parent().parent().andSelf().remove();
            var attributoIdAnswer = ($(this).attr("name")).split("-")[3];

            delete questionAnswers[attributoIdAnswer];
          
        });
	
	
	
	
});
function checkConfirmQuestion() {
	
	var message = "";
	
		
	 if (Object.keys(questionAnswers).length < 2) {
		 message = "Question must have at least two answers";
	    } else {
	    	
	    	sendDataQuestionToServer();
	        return;
	    }

	    BootstrapDialog.show({
	        type: BootstrapDialog.TYPE_DANGER,
	        title: 'Error on create general question',
	        message: message,
	        buttons: [{
	            label: 'Ok',
	            action: function(dialog) {
	                dialog.close();
	                return false;
	            }
	        }]
	    });
	
}

function receiveDataCategoriesFromServer() {
    $.ajax({
        url: 'CreatePacket',
        data: {
            query: 'categories'
        },
        success: function(response) {
            $.each(response, function(index, value) {
                $('#dropdown-packet-categories').append(
                    "<option value=\"" + value.id + "\">" + value.name + "</option>");
            });
            $('#dropdown-packet-categories').multiselect({
                buttonWidth: '100%'
            });
        }
    });
}

function sendDataQuestionToServer() {
    var questionData = {
    	questionTextEng: questionTextEng,
    	questionTextEsp: questionTextEsp,
        answers: questionAnswers
    }

    var jsonStr = JSON.stringify(questionData);
   
    $.ajaxSetup({
        beforeSend: function () {
        	$.blockUI({ 
        		message: "Wait question creation ...",
        		css: { 
                border: 'none', 
                padding: '15px', 
                backgroundColor: '#000', 
                '-webkit-border-radius': '10px', 
                '-moz-border-radius': '10px', 
                opacity: .5, 
                color: '#fff' 
            } }); 
        },
        complete: function () {
        	$.unblockUI;
        }
    });
    $.ajax({
        url: 'CreateGeneralQuestion',
        type: 'post',
        dataType: 'json',
        data: jsonStr,
        error: function() {
        	$.unblockUI();
        	BootstrapDialog.show({
    	        type: BootstrapDialog.TYPE_DANGER,
    	        title: 'Question',
    	        message: 'Error creating general question',
    	        buttons: [{
    	            label: 'Ok',
    	            action: function(dialog) {
    	                dialog.close();
    	                return false;
    	            }
    	        }]
    	    });
        },
        success: function(data) {
        	$.unblockUI();
        	 BootstrapDialog.show({
        	        type: BootstrapDialog.TYPE_SUCCESS,
        	        title: 'Question',
        	        message: 'General question created!',
        	        buttons: [{
        	            label: 'Ok',
        	            action: function(dialog) {
        	            	 window.location = 'index.jsp';
        	                dialog.close();
        	                return false;
        	            }
        	        }]
        	    });
        }
    });
}
