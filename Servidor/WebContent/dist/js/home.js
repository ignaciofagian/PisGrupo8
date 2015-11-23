
var allPacketDetails = {};
var totalPackets = 0;
var totalPages = 0;

$(document).ready(function() { 
	
	getCountersFromServer();
	getAllPacketDetailsFromServer();
	
	totalPackets = Object.keys(	allPacketDetails).length;
	
	totalPages = Math.floor(totalPackets / 8);
	
	if (totalPackets % 8 != 0)
		totalPages++;
	
	paginationGenerate(1,totalPages);
	  $('body').scrollTop(2);
})

function paginationGenerate(page,pageCount) {
	 generatePageView(page);   
	var pagination = "";
	    pagination += "<div id=\"paginator\" class=\"pull-right dataTables_paginate paging_simple_numbers\">";
	    pagination +="	<ul class=\"pagination\">";
	    pagination +=" 		<li style=\"cursor: pointer;\" class=\"paginate_button previous" +  ((page != 1) ? "" : " disabled") + "\" tabindex=\"0\" id=\"dataTables-example_previous\">";
	    pagination +="			 <a onclick=\"paginationGenerate(checkPrevious(" + page + "," + pageCount + ")," + pageCount + ")\">Previous</a>";
	    pagination +="		</li>";
        
	    /* Page Range Calculation */
	    var range = pageRange(page, pageCount);
	    var start = range.start;
	    var end = range.end;
 
	    for (var page_id = start; page_id <= end; page_id++) {
	        if (page_id != page) 
	        	pagination += "<li style=\"cursor: pointer;\" class=\"paginate_button\" tabindex=\"0\"><a  onclick=\"paginationGenerate(" + page_id + "," + pageCount + ")\">" + page_id + "</a></li>"
	        	
	        else 
	        	pagination += "<li style=\"cursor: pointer;\" class=\"paginate_button active\" tabindex=\"0\"><a>" + page_id + "</a></li>";	        
	    }
	    
	    pagination +=  "		<li style=\"cursor: pointer;\" class=\"paginate_button next "  + ((page != pageCount) ? "" : "disabled") + "\" tabindex=\"0\" id=\"dataTables-example_next\">";
	    pagination +=  "			<a onclick=\"paginationGenerate(checkNext(" + page + "," + pageCount + ")," + pageCount + ")\">Next</a>";
	    pagination +=  "		</li>"; 
	    pagination +=  "	</ul>";
	    pagination +=  "</div>"; 
	    	    
	    $('#paginator').remove();
	    $('.container-fluid').append(pagination);
	    $('body').scrollTop(1000);
	}

function generatePageView(page)
{
	 if ( $("#packet-detail-row-1").length)
	    $("#packet-detail-row-1").remove();
	    
	  if ( $("#packet-detail-row-2").length)
	    $("#packet-detail-row-2").remove();
	    
	var html = "";
	
	for (var i = 1; (i<=2) && ((page-1)*8) + (i-1) <= totalPackets ; i++){
		
		html += "<div id=\"packet-detail-row-" + i + "\"" + "class=\"row row-packets\">";
		
		for (var j = 1 ; (j<=4) && ((page-1)*8) + ((i-1)*4) + (j-1)  < totalPackets; j++)
		{
			
			html += createPacketPanelView(allPacketDetails[((page-1)*8) + ((i-1)*4) + (j-1)]);
		}
		html += "</div>";
	}
	$('.container-fluid').append(html);
}

function getCountersFromServer()
{
	$.ajax({
        url: 'Home',
        data: {
            query: 'counters',
        },
        success: function(response) {
        	$("#label-users-count").text(response.clientCount); 
        	$("#label-packets-count").text(response.packetCount);   
        	$("#label-stocks-count").text(response.actionCount);   
         },
        async : true
    });
}

function getAllPacketDetailsFromServer()
{
	$.ajax({
        url: 'Home',
        data: {
            query: 'packetDetailList',
        },
        success: function(response) {
        	$.each(response, function(index, value) {
        		allPacketDetails[index] = value;
        	})       	
         },
        async : false
    });
}

function createPacketPanelView(packetDetail)
{
	if (packetDetail.tipo == 'Indice')
		packetDetail.tipo = 'Index';
	else
		packetDetail.tipo = 'Algoritmic';
	
	var html = "";
	html += "<div class=\"col-lg-3 col-md-6\">";
	html += "<a href=\"PacketList?query=packetView&id=" + packetDetail.idPaquete + "\">";
	html += "    <div class=\"panel panel-info\">";
	html += "        <div class=\"panel-heading\">";
	html += "            " + packetDetail.nombre;
	html += "        </div>";
	html += "         <div class=\"panel-body\" style=\"height: 140px;\">";
	html += "            <h4>" + packetDetail.tipo + "</h4>";
	html += "            <ul>";
	
	var i = 0;
	
	for (var i= 0; i<3; i++){	
		if (Object.keys(packetDetail.acciones).length > i)
			if (i == 2){
	    		html += "<li>...</li>";
	    	}else{
	    		html += "<li>" +  packetDetail.acciones[i].simbolo + " - " + packetDetail.acciones[i].porcentaje + "%" + "</li>";
	    	}			
	}
	
    html += "             </ul>";
    html += "        </div>";
    html += "         <div class=\"panel-footer\">";
    html += "             Investors " + packetDetail.cantidadInversionistas + " users";
    html += "          </div>";
    html += "       </div>";
    html += "   </a>";
    html += "</div>";
    return html;
}
	

/* Pagination Navigation */
function checkPrevious(id) {
    if (id > 1) {
        return (id - 1);
    }
    return 1;
}

/* Pagination Navigation */
function checkNext(id, pageCount) {
    if (id < pageCount) {
        return (id + 1);
    }
    return id;
}

function pageRange(page, pageCount) {

    var start = page - 2,
        end = page + 2;

    if (end > pageCount) {
        start -= (end - pageCount);
        end = pageCount;
    }
    if (start <= 0) {
        end += ((start - 1) * (-1));
        start = 1;
    }

    end = end > pageCount ? pageCount : end;

    return {
        start: start,
        end: end
    };
}