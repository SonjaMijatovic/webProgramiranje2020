$(document).ready(function(){

	//odustajanje od prijave korisnika
	$("#odustani").click(function(event){
		event.preventDefault();
		window.location.href = "index.html";
	})

	let allTds = $("td");
	
	//validacija za korisnicko ime
	let tag = document.createElement("p");
	tag.setAttribute("id", "prvi");
	let text = document.createTextNode("Filed can not be empty!");
	tag.append(text);	
	tag.style.color = 'red';
	let obojeniTekst = tag;
	allTds[1].after(obojeniTekst);
	document.getElementById("prvi").hidden = true;
		
	//validacija za lozinku
	let tag2 = document.createElement("p");
	tag2.setAttribute("id", "drugi");
	let text2 = document.createTextNode("Filed can not be empty!");
	tag2.append(text2);	
	tag2.style.color = 'red';
	let obojeniTekst2 = tag2;
	allTds[3].after(obojeniTekst2);
	document.getElementById("drugi").hidden = true;
	
	//$("#prijavi").off();
	$("#prijavi").click(function(event){
		event.preventDefault();
		
		let kor_ime = $("#korIme").val();
		let loz = $("#loz").val();

		if(kor_ime == ""){
			document.getElementById("prvi").hidden = false;
		}else {
			document.getElementById("prvi").hidden = true;
		}

		if(loz == ""){
			document.getElementById("drugi").hidden = false;
		}else {
			document.getElementById("drugi").hidden = true;
		}
		
		//slanje zahteva za login korisnika na server
		$.ajax({
			type: 'POST',
			url: 'rest/user/login/'+kor_ime+'/'+loz,
			complete: function(data){
				
				if(data["status"] == 200){
					window.location.href = "index.html";
				}else if(data["status"] == 500){
					alert("Username or password is not correct");
				}else {
					console.log("Login unsuccessful");
				}
			}
		})
		
	})
	
});