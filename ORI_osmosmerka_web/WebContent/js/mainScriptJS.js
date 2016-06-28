var startClick = 0; //not clicked
var endClick = 0; //not clicked


function getURLParameter(url, name) {
    return (RegExp(name + '=' + '(.+?)(&|$)').exec(url)||[,null])[1];
}


function random()
{

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
				$(divID).addClass("plain_word");
				
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
			alert("AJAX ERROR in get game table js: " + errorThrown);
		}
	});
	
}

$(document).on('mousedown','.letters',function(){
	if (startClick == 0){
		var selectedLetter = $(this).text();
		$('#showSelectedLetters').val(selectedLetter);
		//$(this).css("background", "#555555");
		
		//TODO treba da se doda klasa
		var is_founded = false;
		
		if ($(this).hasClass('founded_word')) {
			$(this).addClass("founded");
        }
		
		$(this).removeClass("plain_word");
		$(this).removeClass("founded_word");
		
		$(this).addClass("selected_word");
		
		
		
		
		startClick = 1; // clicked
		endClick = 0;
	}
	else if (startClick == 1 && endClick ==0){
		startClick = 0;
		endClick = 1;
		
		//TODO pozvati funkciju koja ce proveriti tacnost 
		
		var selectedLetters = $('#showSelectedLetters').val();
		
		$.ajax({
			type : 'POST',
			url : "../ORI_osmosmerka_web/rest/game/checkAnswer",
			contentType : 'application/json',
			data : JSON.stringify(selectedLetters),
			dataType : "text", 				// data type of response
			success : function(data) {
				if (data == "ok") //ako je pogodio rec obelezi je kao pronadjenu
				{
					$("." + selectedLetters).html("<strike  style='color:#555555'>" + selectedLetters + "</strike>");
					
					$(".selected_word").addClass("founded_word");
					$(".selected_word").removeClass("selected_word");
				}
				else
				{ //ako nije ponisti svima boju
					$(".selected_word").removeClass("selected_word");
					$(".founded").addClass("founded_word");
				}
				
			},
			error:  function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR in all Objects Index js: " + errorThrown);
			}
		});
		
		
		
		
	}
	
})

$(document).on('mouseover','.letters', function(){
	if (startClick == 1 && endClick == 0){
		var text = $('#showSelectedLetters').val();
		var selectedLetter = $(this).text();
		//$(this).css("background", "#555555");
		$('#showSelectedLetters').val(text + selectedLetter);
		
		var is_founded = false;
		
		if ($(this).hasClass('founded_word')) {
			$(this).addClass("founded");
        }
		
		$(this).removeClass("plain_word");
		$(this).removeClass("founded_word");
		
		$(this).addClass("selected_word");
	}
});

