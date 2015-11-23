$(document).ready(function() {

      $("#input-search-stock ").autocomplete({
    	  
    	  search  : function(){$(this).addClass('working-indicator');},
    	  response    : function(){$(this).removeClass('working-indicator');},
    	  source: function(request, response) {
            $.ajax({
                url: "http://d.yimg.com/aq/autoc",
                dataType: "jsonp",
                data: {
                    "query" : request.term,
                    "region": "US",
                    "lang": "en-US",
                    "callback" : "YAHOO.util.ScriptNodeDataSource.callbacks"
                },
                success: function(data) {
                    var suggestions = [];

                    $.each(data.ResultSet.Result, function(i, val) {
                        suggestions.push(val.symbol + ' ' +  val.name);
                    });

                    response(suggestions);

                }
            });
        },
        change: function(event, ui) {
            if (ui.item) {
            	wasSelectedItemFromAutocomplete = true;
            	isDuplicatedItemFromAutocomplete = false;
            	for (var key in packetStocks) 
            	{
            		if (packetStocks[key].symbol == ui.item.value.split(" ")[0])
	            	{
            			isDuplicatedItemFromAutocomplete = true;
            			break;
	            	}
            	}
            	$(this).valid();
            } else {
            	wasSelectedItemFromAutocomplete = false;
            }
        }
    });


   



});
