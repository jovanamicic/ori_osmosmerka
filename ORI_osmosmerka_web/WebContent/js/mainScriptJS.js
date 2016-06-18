function random()
{
	for (var i = 0; i < 12*12; i++) 
	{
		var divID = "#" + i;
		$(divID).html('A');
	}
}