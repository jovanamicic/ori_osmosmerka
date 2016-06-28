function chooseCategory(e){
	$('#myModal').modal();
}

category = "";


$(document).on('click', '#chooseCategory li', function(){
	category = $(this).text();
	$('#chooseCategoryBtn').html(category);
});

function redirect(){
	window.location.href = "template.html?category=" + category;
}