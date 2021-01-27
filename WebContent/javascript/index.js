$(document).ready(function(){
	
	
	$("#prijava_button").click(function(event)
	{
		event.preventDefault();
		window.location.href = "login.html";
	})
	
	$("#registracija_button").click(function(event){
		event.preventDefault();
		window.location.href = "registration.html";
	})
	
	
});
