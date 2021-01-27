$(document).ready(function(){

	$('#greskaKorImePor').hide();
	$('#greskaLozPor').hide();
	$('#greskaPonLozPor').hide();
	$('#greskaImePor').hide();
	$('#greskaPrzPor').hide();

	var greskaKorIme = false;
	var greskaLoz = false;
	var greskaPonLoz = false;
	var greskaIme = false;
	var greskaPrz = false;

	$('#korIme').focusout(function() {
		proveriKorIme();
	});

	$('#loz').focusout(function() {
		proveriLozinku();		
	});

	$('#ponLoz').focusout(function() {
		proveriPonLozinku();
	});

	$('#imeKorisnika').focusout(function() {
		proveriIme();
	});

	$('#przKorisnika').focusout(function() {
		proveriPrezime();
	});

	function proveriKorIme() {
		var korImeDuz = $('#korIme').val().length;

		if (korImeDuz < 5 || korImeDuz > 20) {
			$('#greskaKorImePor').html("Username needs to be between 5 and 20 caracters.");
			$('#greskaKorImePor').show();
			greskaKorIme = true;
		} else {		
			$('#greskaKorImePor').hide();
		}
	}

	function proveriLozinku() {
		var lozinkaDuz = $('#loz').val().length;

		if (lozinkaDuz < 5) {
			$('#greskaLozPor').html("Password needs to contain at least 5 caracters.");
			$('#greskaLozPor').show();
			greskaLoz = true;
		} else {
			$('#greskaLozPor').hide();
		}

	}

	function proveriPonLozinku() {
		var lozinka = $('#loz').val();
		var ponLozinka = $('#ponLoz').val();

		if (lozinka != ponLozinka) {
			$('#greskaPonLozPor').html("Password doesn't match.");
			$('#greskaPonLozPor').show();
			greskaPonLoz = true;
		} else {
			$('#greskaPonLozPor').hide();
		}

	}

	function proveriIme() {
		var ime = $('#imeKorisnika').val();
		
		if (ime == "") {
			$('#greskaImePor').html("Enter firstname");
			$('#greskaImePor').show();
			greskaIme = true;
		} else {
			$('#greskaImePor').hide();
		}

	}

	function proveriPrezime() {
		var prz = $('#przKorisnika').val();
		
		if (prz == "") {
			$('#greskaPrzPor').html("Enter lastname");
			$('#greskaPrzPor').show();
			greskaPrz = true;
		} else {
			$('#greskaPrzPor').hide();
		}

	}

	$('#registruj').click(function(event) {
		event.preventDefault();

		greskaKorIme = false;
		greskaLoz = false;
		greskaPonLoz = false;
		greskaIme = false;
		greskaPrz = false;

		proveriKorIme();
		proveriLozinku();
		proveriPonLozinku();
		proveriIme();
		proveriPrezime();
		

		if (greskaKorIme == false && greskaLoz == false && greskaPonLoz == false && greskaIme == false && greskaPrz == false) {
			
			
			let podaci = {
				"username": $('#korIme').val(),
				"password": $('#loz').val(),
				"firstname": $('#imeKorisnika').val(),
				"lastname": $('#przKorisnika').val(),
				"gender": $('#pol option:selected').text()
			};
			
			var s = JSON.stringify(podaci);
			
		//	alert(s);

			$.ajax ({
				url: 'rest/user/register',
				type: 'POST',
				data: s,
				contentType: 'application/json',
				dataType: 'json',
				complete: function(data) {
					if (data["status"] == 200) {
						alert("User is registered and logged in!");
						window.location.href = "index.html";
					} else if(data["status"] == 500){
						alert("Username is already used.");
					} else {
						alert("Registration unsuccessful!");
					}
				}
			});
		} else {
			alert("You have to fill all fields.");
		}
				
});
	//odustaje se od registracije i vraca se na pocetnu stranicu
	$("#odustani").click(function(event){
		event.preventDefault();
		window.location.href = "index.html";
	})
});