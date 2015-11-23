function getTotalStockPorcentages()
    {
        var totalPorcentages = 0;
        $('#step-1').find('table').find('tbody').find('tr').each(function (i, el) {
            var $tds = $(this).find('td');
            var porcentage = $tds.eq(2).text();

            var num = porcentage.substring(0, porcentage.length - 1);
            totalPorcentages += parseFloat(num);
        });
        
        return totalPorcentages; 
    }
    

$(document).ready(function() {


    $.validator.addMethod("needsSelection", function(value, element) {
        var count = $(element).find('option:selected').length;
        return count > 0;
    });

    $.validator.addMethod("autocompleteNeedsSelecction", function(value, element) {
        return wasSelectedItemFromAutocomplete;
    });
    
    $.validator.addMethod("stockDuplicatedValue", function(value, element) {
        return !isDuplicatedItemFromAutocomplete;
    });

     $.validator.addMethod("totalPorcentageStocks", function(value, element) {
        
        return (getTotalStockPorcentages() + parseFloat(value)) <= 100;
    });

    $(document).on('click', "[name='btn-add-answer']", function() {
        $(this).parent().parent().validate({
            rules: {
                answerEng: {
                    minlength: 5,
                    required: true
                },
                answerEsp: {
                    minlength: 5,
                    required: true
                },
                answerPoints: {
                    required: true,
                    digits : true,
                    range: [0, 10]
                }
            },
            messages: {
                stockPorcentage: {
                    range: "Must be a number in range [0,10]"

                }
            }
           
        });

    });

        $('#panelSpecificQuestions').validate({
            rules: {
                questionEng: {
                    minlength: 5,
                    required: true
                },
                questionEsp: {
                    minlength: 5,
                    required: true
                }
            },

            errorElement: "small", // contain the error msg in a small tag
            wrapper: "div", // wrap the error message and small tag in a div
            highlight: function(element) {
                $(element).closest('.control-group').addClass('error'); // add the Bootstrap error class to the control group  
            },
            success: function(element) {
                $(element).closest('.control-group').removeClass('error'); // remove the Boostrap error class from the control group
            }
        })
 

      
        $('#panelStocks').validate({
            rules: {
                stockName: {                 
                    required: true,
                    autocompleteNeedsSelecction : true,
                    stockDuplicatedValue : true
                },
                stockPorcentage: {
                    required: true,
                    digits : true,
                    range: [1, 100],
                    totalPorcentageStocks : true
                }
            },
            messages: {
                stockPorcentage: {
                    range: "Must be a namber in range [1,100]",
                    totalPorcentageStocks : "The total sum of stock must be less than 100%"

                },stockName: {
                    autocompleteNeedsSelecction : "Must select a stock from list" ,
                    stockDuplicatedValue : "This stock is already in list"
                }
            
            },
            errorPlacement: function(error, element) {
                if (element.attr("name") == "stockPorcentage") {
                	error.css("margin-top", "-14px");
                    element.parent().parent().append(error);
                    
                } else {

                    error.insertAfter(element);
                }
            }
        })

    
        $('#panelPacketDetails').validate({
            ignore: [],
            rules: {
                packetCategories: {
                    needsSelection: true
                },
                packetName: {
                    minlength: 1,
                    required: true
                 
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
            
        })
  
});
