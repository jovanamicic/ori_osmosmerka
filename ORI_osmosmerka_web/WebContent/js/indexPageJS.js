function chooseCategory(e){
	$('#myModal').modal();
}

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