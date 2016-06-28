function chooseCategory(e){
	$('#myModal').modal();
}

category = "";


$(document).on('click', '#chooseCategory li', function(){
	category = $(this).text();
	$('#chooseCategoryBtn').html(category);
	alert(category);
});

function redirect(){
	
	$.ajax({
		type : 'POST',
		url : "../ORI_osmosmerka_web/rest/game/getGameTable",
		contentType : 'application/json',
		data : JSON.stringify({
			"kategorija" : "kategorija"
		}),
		dataType : "json", 				// data type of response
		success : function(data) {
			
			var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
			$.each(list, function(index, field){
				var divID = "#" + index;
				$(divID).html(field.letter);
			});
		},
		error:  function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR in all Objects Index js: " + errorThrown);
		}
	});
	
	window.location.href = "template.html";
}
