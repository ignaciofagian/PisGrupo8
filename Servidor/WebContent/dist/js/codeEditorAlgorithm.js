var editor;

$(document).ready(function() { 
	editor = ace.edit("algorithm-code-editor");
	
	editor.getSession().setMode("ace/mode/plainStock");
	editor.setTheme("ace/theme/eclipse");
	editor.getSession().setTabSize(2);
	editor.getSession().setUseWrapMode(true);
	editor.setOptions({
		fontSize: "16px"
	});
   
});