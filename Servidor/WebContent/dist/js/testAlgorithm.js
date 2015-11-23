/**
 * 
 */


$(document).ready(function(){

	$('#go-next-from-step-3').attr("disabled", true);	
		
	$('#algorithm-code-editor').keypress(function(){
		$('#go-next-from-step-3').attr("disabled", true);
	});
	
	$('#algorithm-code-editor').on('click', function(){
		$('#go-next-from-step-3').attr("disabled", true);
	});
	
	$('#show-errors').on('click', function(e) {
		packetAlgorithm = editor.getValue();
		testAlgorithm();
	});
	
});

function testAlgorithm() {
	var packetData = {
			algorithm : packetAlgorithm
	}

	var jsonStr = JSON.stringify(packetData);

	$.ajaxSetup({
		beforeSend : function() {
			$.blockUI({
				message : "Wait testing algorithm ...",
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
		url : 'TestAlgorithm',
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
			if (data.error != ""){
				BootstrapDialog.show({
					type : BootstrapDialog.TYPE_DANGER,
					title : 'Algorithm interpretation',
					message :'There is an error in your algorithm, please check.',
					buttons : [ {
						label : 'Ok',
						action : function(dialog) {
							dialog.close();
							return false;
						}
					} ]
				});
				
				$('#algorithm-errors').removeClass('hidden');
				$('#algorithm-errors').text(data.error);
				
			}else{			
				BootstrapDialog.show({
					type : BootstrapDialog.TYPE_SUCCESS,
					title : 'Algorithm interpretation',
					message : 'No errors found',
					buttons : [ {
						label : 'Ok',
						action : function(dialog) {
							dialog.close();
							return false;
						}
					} ]
				});
				
				$('#algorithm-errors').text("");
				$('#algorithm-errors').addClass('hidden');	
				$('#go-next-from-step-3').attr("disabled", false);
				
			}
		},
		complete: function(){
			$.unblockUI();
		   }
	});
}