var startClick = 0; //not clicked
var endClick = 0; //not clicked


function getURLParameter(url, name) {
    return (RegExp(name + '=' + '(.+?)(&|$)').exec(url)||[,null])[1];
}


function random()
{

//	for (var i = 0; i < 12*12; i++) 
//	{
//		var divID = "#" + i;
//		$(divID).html(i.toString().charAt(0));
//	}
//	
	var category = getURLParameter(window.location, 'category');
	
	$.ajax({
		type : 'POST',
		url : "../ORI_osmosmerka_web/rest/game/getGameTable",
		contentType : 'application/json',
		data : JSON.stringify(category),
		dataType : "json", 				// data type of response
		success : function(data) {
			
			var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
			$.each(list, function(index, field){
				var divID = "#" + index;
				$(divID).html(field.letter);
				
			});
			wordsToFind();
		},
		error:  function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR in all Objects Index js: " + errorThrown);
		}
	});
}


function wordsToFind() {
	
	var divForWords = document.getElementById("allWords");
	
	$.ajax({
		type : 'POST',
		url : "../ORI_osmosmerka_web/rest/game/getWords",
		contentType : 'application/json',
		dataType : "json", 				// data type of response
		success : function(data) {
			
			var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
			$.each(list, function(index, word){
				
				var innerDiv = document.createElement('div');
				innerDiv.className = word;

				divForWords.appendChild(innerDiv);
				innerDiv.innerHTML = word;
				
			});
		},
		error:  function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR in all Objects Index js: " + errorThrown);
		}
	});
	
}

$(document).on('mousedown','.letters',function(){
	if (startClick == 0){
		var selectedLetter = $(this).text();
		$('#showSelectedLetters').val(selectedLetter);
		$(this).css("background", "#555555");
		startClick = 1; // clicked
		endClick = 0;
	}
	else if (startClick == 1 && endClick ==0){
		startClick = 0;
		endClick = 1;
		//TODO pozvati funkciju koja ce proveriti tacnost 
		alert("check answer");
		//ako je pogodio rec obelezi je kao pronadjenu
		//ako nije ponisti svima boju
		for (var i = 0; i < 12*12; i++) 
		{
			var divID = "#" + i;
			$(divID).css("background", "#1abc9c");
		}
	}
	
})

$(document).on('mouseover','.letters', function(){
	if (startClick == 1 && endClick == 0){
		var text = $('#showSelectedLetters').val();
		var selectedLetter = $(this).text();
		$(this).css("background", "#555555");
		$('#showSelectedLetters').val(text + selectedLetter);
	}
});

