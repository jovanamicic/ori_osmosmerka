var startClick = 0; //not clicked
var endClick = 0; //not clicked
var foundWordsCount = 0;
var wordsToFindCount = 0;
var numberOfSelected = 0; //broj selektovanih divova, kako bi se omogucila zabrana selektovanja u pogresnom smeru
var id1 = 0;
var id2 = 0;
var direc = "";
category = "";
difficult = "";

$(document).on('click', '#chooseCategory li', function(){
	category = $(this).text();
	$('#chooseCategoryBtn').html(category);
});

$(document).on('click', '#chooseDifficult li', function(){
	difficult = $(this).text();
	$('#chooseDifficultBtn').html(difficult);
});

function redirect(){
	window.location.href = "template.html?category=" + category + "&difficult=" + difficult;
}


function getURLParameter(url, name) {
    return (RegExp(name + '=' + '(.+?)(&|$)').exec(url)||[,null])[1];
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,    
    function(m,key,value) {
      vars[key] = value;
    });
    return vars;
  }


function random()
{
	category = getUrlVars()["category"];
	difficult = getUrlVars()["difficult"];
	
	//defaultna kategorija
	if (category == ""){
		category = "Computer";
	}
	
	if (difficult == ""){
		difficult = "Hard";
	}
	
	$.ajax({
		type : 'POST',
		url : "../ORI_osmosmerka_web/rest/game/getGameTable",
		contentType : 'application/json',
		data : JSON.stringify({
			"category" : category,
			"difficult" : difficult
		}),
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
				wordsToFindCount++;
			});
		},
		error:  function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR in get game table js: " + errorThrown);
		}
	});
	
}

var firstLetterID = -1;
$(document).on('mousedown','.letters',function(){
	if (startClick == 0){
		var selectedLetter = $(this).text();
		$('#showSelectedLetters').val(selectedLetter);
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
					var selectedWord = selectedLetters.toLowerCase();
					$("." + selectedWord).html("<strike  style='color:#555555' class='strikeClass'>" + selectedWord + "</strike>");
					
					$(".selected_word").addClass("founded_word");
					var divId = "#"+ firstLetterID;
					$(".selected_word").removeClass("selected_word");
					$(divId).removeClass("icon-effect");
					var snd = new Audio("sound//successSound.mp3");
					snd.play();
					
					foundWordsCount++;
					if  (wordsToFindCount == foundWordsCount){
						toastr.success("Congrats! You win!");  //TODO mozemo dodati neki lepsi ispis...
						$("#stop").click();
						var snd = new Audio("sound//gameOverSound.mp3");
						snd.play();
						setTimeout(function(){$('#playAgainModal').modal();}, 2000);
						
						
					}
				}
				else
				{ //ako nije ponisti svima boju
					$(".selected_word").removeClass("selected_word");
					$(".founded").addClass("founded_word");
					var snd = new Audio("sound//errorSound.mp3");
					snd.play();
					$('#showSelectedLetters').val("");
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
		
		
		var is_founded = false;
		
		
		
		if ($(this).hasClass('founded_word')) {
			$(this).addClass("founded");
        }
		
		$(this).removeClass("plain_word");
		$(this).removeClass("founded_word");
		
		console.log("duzina " + $('.selected_word').length);
		
		if ($(this).hasClass("selected_word"))
		{
			$("#"+previousDivID).removeClass("selected_word");
			$('#showSelectedLetters').val(text.substring(0, text.length-1));
			if ($("#"+previousDivID).hasClass('founded')) {
				$("#"+previousDivID).addClass("founded_word");
			}
		}
		else if ($('.selected_word').length < 2)
		{
			console.log("usao u prvi else");
			
			$('#showSelectedLetters').val(text + selectedLetter);
			$(this).addClass("selected_word");
			
			
			numberOfSelected = $('.selected_word').length;
			
			//ukoliko je broj selektovanih divova veci ili jednak 2
			//uoci patern i onemoguci selektovanje ostalih divova
			if (numberOfSelected >= 2) {
				id1 = document.getElementsByClassName('selected_word')[0].id;
				id2 = document.getElementsByClassName('selected_word')[1].id;
				
				if (Number(id1) + 1 == Number(id2)){
					direc = "horizontal";
					console.log(direc);
				}
				else if (Number(id1) + 12 == Number(id2)){
					direc = "vertical";
					console.log(direc);
				}
				else if (Number(id1) + 12 - 1 == Number(id2)){
					direc = "diagonal/ ";
					console.log(direc);
				}
				else if (Number(id1) + 1 + 12 == Number(id2)){
					direc = "diagonal\ ";
					console.log(direc);
				}
			}
		}
		else {
			if ($('.selected_word').length >= 2){
				
				if (direc == "horizontal"){
					//ide ka desno, EAST
					if (Number($(this).attr("id")) == Number(id2) + 1) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id2 = Number($(this).attr("id"));
					}
					//ide ka levo, WEST
					if (Number($(this).attr("id")) + 1 == Number(id1) ) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id1 = Number($(this).attr("id"));
					}
				}
				else if (direc == "vertical") {
					//ide ka dole, SOUTH
					if (Number($(this).attr("id")) == Number(id2) + 12) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id2 = Number($(this).attr("id"));
					}
					//ide ka gore, NORTH
					if (Number($(this).attr("id")) + 12 == Number(id1) ) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id1 = Number($(this).attr("id"));
					}
				}
				
				else if (direc == "diagonal/ ") {
					//ide ka gore desno, SOUTH-EAST
					if (Number($(this).attr("id")) == Number(id2) + 12 - 1) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id2 = Number($(this).attr("id"));
					}
					//ide ka gore, NORTH
					if (Number($(this).attr("id")) + 12 - 1 == Number(id1) ) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id1 = Number($(this).attr("id"));
					}
				}
				
				else if (direc == "diagonal\ ") {
					//ide ka gore desno, SOUTH-EAST
					if (Number($(this).attr("id")) == Number(id2) + 12 + 1) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id2 = Number($(this).attr("id"));
					}
					//ide ka gore, NORTH
					if (Number($(this).attr("id")) + 12 + 1 == Number(id1) ) {
						$('#showSelectedLetters').val(text + selectedLetter);
						$(this).addClass("selected_word");
						id1 = Number($(this).attr("id"));
					}
				}
			}
		}
	}
});

var previousDivID = "";
$(document).on('mouseleave','.letters', function(){
	previousDivID = $(this).attr('id');
});


function startTimer() {
	var h1 = document.getElementsByTagName('h1')[0],
	start = document.getElementById('start'),
	stop = document.getElementById('stop'),
	clear = document.getElementById('clear'),
	seconds = 0, minutes = 0, hours = 0,
	t;

	function add() {
	seconds++;
	if (seconds >= 60) {
	    seconds = 0;
	    minutes++;
	    if (minutes >= 60) {
	        minutes = 0;
	        hours++;
	    }
	}

	h1.textContent = (hours ? (hours > 9 ? hours : "0" + hours) : "00") + ":" + (minutes ? (minutes > 9 ? minutes : "0" + minutes) : "00") + ":" + (seconds > 9 ? seconds : "0" + seconds);

	timer();
	}
	function timer() {
	t = setTimeout(add, 1000);
	}
	timer();
	
	stop.onclick = function() {
	    clearTimeout(t);
	}
}

var hintUsed = 0;
//***SHINE*** Klikom na hint zasija prvo slovo reci koja nije pronadjena
 $(document).on('click','#hintBtn',function(e){
	 hintUsed++;
	 var notFoundWords = [];
	 var hintWord = "";
	 e.preventDefault();
	 $("#allWords > div").each(function(){
		    var context = $(this);
		    if (!($(this).has('strike').length)) {
		    	notFoundWords.push(context.text());
		    }
		});
	 hintWord = notFoundWords[0];
	 
	 if (hintUsed == 5)
		 toastr.info("You have no more easy hints!");
	 
	 var urlForHint = "";	 
	 if (hintUsed <= 5){
		urlForHint =  "../ORI_osmosmerka_web/rest/game/findLetterForHintEasy";
	 }
	 else
		 urlForHint =  "../ORI_osmosmerka_web/rest/game/findLetterForHintHard";
	 
	 $.ajax({
			type : 'POST',
			url : urlForHint ,
			contentType : 'application/json',
			data : JSON.stringify(hintWord),
			dataType : "text", 				// data type of response
			success : function(data) {
				divId = "#"+data;
				$(divId).addClass("icon-effect");
				firstLetterID = data;
			},
			error:  function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR in all Objects Index js: " + errorThrown);
			}
		});
	 
 }) 
 
  //SOLVE Btn
 $(document).on('click','#solveBtn',function(e){
	 $.ajax({
			type : 'GET',
			url : "../ORI_osmosmerka_web/rest/game/solve" ,
			dataType : "json", 				
			success : function(data) {
				var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
				$.each(list, function(index, letter){
					var divID = "#" + letter;
					$(divID).removeClass("plain_word");
					$(divID).addClass("founded_word");
				});
				
				//precrtaj pronadjene reci
				$.ajax({
					type : 'GET',
					url : "../ORI_osmosmerka_web/rest/game/solvedWords" ,
					dataType : "json", 				
					success : function(data) {
						var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
						$.each(list, function(index, word){
							$("." + word).html("<strike  style='color:#555555' class='strikeClass'>" + word + "</strike>");
						});
					},
					error:  function(XMLHttpRequest, textStatus, errorThrown) {
						alert("AJAX ERROR in all Objects Index js: " + errorThrown);
					}
				});
				
				foundWordsCount = 12;
				if  (wordsToFindCount == foundWordsCount){
					toastr.success("Congrats! You win!");  //TODO mozemo dodati neki lepsi ispis...
					$("#stop").click();
					var snd = new Audio("sound//gameOverSound.mp3");
					snd.play();
					setTimeout(function(){$('#playAgainModal').modal();}, 2000);
				}
			},
			error:  function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR in all Objects Index js: " + errorThrown);
			}
		});
	 
	 
 })
