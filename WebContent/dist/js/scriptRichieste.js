$("#chiudiRichiesta, #chiudiRichiesta1").click(function()
{
	$("#richiesta").find("input, textarea").val("");
});


$(".richiediPrestazione").click(function(event)
{
	$("#modal-default").modal("show");
	var formId = $(this).attr("id").replace("richiediPrestazione", "");
	
	
	$("#modal-default").on("hidden.bs.modal", function () 
	{
		alert(formId);
		if($("#richiesta").find("input, textarea").val() == "")
		{
			delete formId;
			$("#richiesta").find("input, textarea").val("");
			return;	
		}
		
		
		$("#richiesta").find(":input").clone().appendTo("#risultato" + formId);
		$("#richiesta").find("input, textarea").val("");
		
		
		var head= document.getElementsByTagName("body")[0];
		var script= document.createElement("script");
		script.src= "dist/js/scriptRichieste.js";
		head.appendChild(script);
	});
	
	
	return false;
});


$("#richiesta").submit(function(e)
{
	$("#modal-default").modal("hide");
    e.preventDefault();
});