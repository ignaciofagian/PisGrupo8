ace.define('ace/mode/plainStock', function(require, exports, module) {

	var oop = require("ace/lib/oop");
	var TextMode = require("ace/mode/text").Mode;
	var Tokenizer = require("ace/tokenizer").Tokenizer;
	var plainStockHighlightRules = require("ace/mode/plainStock_highlight_rules").plainStockHighlightRules;
	
	var Mode = function() {
	    this.$tokenizer = new Tokenizer(new plainStockHighlightRules().getRules());
	};
	oop.inherits(Mode, TextMode);
	
	(function() {
	    // Extra logic goes here. (see below)
	}).call(Mode.prototype);
	
	exports.Mode = Mode;
});




ace.define('ace/mode/plainStock_highlight_rules', function(require, exports, module) {

	var oop = require("ace/lib/oop");
	var TextHighlightRules = require("ace/mode/text_highlight_rules").TextHighlightRules;
	
	var plainStockHighlightRules = function() {
	
		var keywords = (
	        "if|then|else|endif|VAR|ENDVAR|and|or|not|mod"
	    );
	
	    var builtinConstants = (
	        "true|false"
	    );
	
	    var builtinFunctions = (
	        "BUY|SELL|avg|dev|HISTORY"
	    );
	
	    var dataTypes = (
	        "Boolean, Float"
	    );
	
	
	    var keywordMapper = this.createKeywordMapper({
	        "support.function": builtinFunctions,
	        "keyword": keywords,
	        "constant.language": builtinConstants,
	        "storage.type": dataTypes
	    }, "identifier", true);
	
	    this.$rules = {
	        "start" : [ {
	            token : "comment",
	            regex : "//.*$"
	        },  {
	            token : "comment",
	            start : "/\\*",
	            end : "\\*/"
	        },	{
	            token : "constant.numeric", // float
	            regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
	        }, {
	            token : keywordMapper,
	            regex : "[a-zA-Z_$][a-zA-Z0-9_$]*\\b"
	        }, {
	            token : "keyword.operator",
	            regex : "\\+|\\-|\\/|\\/\\/|%|<@>|@>|<@|&|\\^|~|<|>|<=|=>|==|!=|<>|="
	        }, {
	            token : "paren.lparen",
	            regex : "[\\(]"
	        }, {
	            token : "paren.rparen",
	            regex : "[\\)]"
	        }, {
	            token : "text",
	            regex : "\\s+"
	        } ]
	    };
	    this.normalizeRules();
	}
	
	oop.inherits(plainStockHighlightRules, TextHighlightRules);
	
	exports.plainStockHighlightRules = plainStockHighlightRules;
});