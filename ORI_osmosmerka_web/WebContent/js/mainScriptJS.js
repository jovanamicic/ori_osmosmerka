var startClick = 0; //not clicked
var endClick = 0; //not clicked

function random()
{
	for (var i = 0; i < 12*12; i++) 
	{
		var divID = "#" + i;
		$(divID).html(i.toString().charAt(0));
	}
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

