$(document).ready(function(){
	$('#pozdravPor').hide();

	$.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data){
			odlogujSe();
			pozdravPoruka(data.responseJSON);
			sakrijDugmad(data.responseJSON);
			dodatneOpcije(data.responseJSON);
			prikazApartmana(data.responseJSON);
		}		
	})
});


function sakrijDugmad(korisnik){
	
	if(korisnik == undefined){
		$("#login_buttons").show();
		$("#acc_buttons").hide();
	}else {
		$("#login_buttons").hide();
		$("#acc_buttons").show();
	}	
}

function dodatneOpcije(korisnik){
	
	if(korisnik == undefined)
		return;

	if(korisnik.role == "ADMIN"){
		$("#acc_buttons").append("<button type='submit' id='korisnici_Btn' onclick=pregledKorisnika()>Pregled korisnika </button> <br/>");
		$("#acc_buttons").append("<button type='submit' id='sadrzaj_Btn' onclick=pregledSadrzajaApartmana()> Pregled sadržaja apartmana </button><br/>");
		$("#acc_buttons").append("<button type='submit' id='rezervacije_Btn' onclick=reservationOverview()> Reservation overview </button><br/>");
		$("#acc_buttons").append("<button type='submit' id='komentari_Btn' onclick=pregledKomentara()> Pegled svih komentara </button> <br/>");
	} else if(korisnik.role == "GUEST"){
		$("#acc_buttons").append("<button type='submit' id='rezervacije' onclick=reservationOverview()> My reservations </button> <br/>");
	} else if(korisnik.role == "HOST"){
		$("#acc_buttons").append("<button type='submit' id='dodajApp_Btn' onclick=dodajApartman()>Add apartment </button> <br/>");
		$("#acc_buttons").append("<button type='submit' id='rezervacije_Btn' onclick=reservationOverview()> My reservations </button><br/>");
	}
}

function odlogujSe(){
	
	$("#odjava_btn").click(function(event){
		event.preventDefault();
		
		$.ajax({
			type: 'POST',
			url: 'rest/user/logout',
			complete: function(data){
				window.location.href = "login.html";
			}
		})
	});
	
	$("#nalog_btn").click(function(event){
		event.preventDefault();
	
		window.location.href = "account.html";
	});
}

function reservationOverview(){
	window.location.href = "reservationOverview.html";
}

function pregledKorisnika(){
	window.location.href = "pregledKorisnika.html";
}

function dodajApartman() {
	window.location.href = "addApartment.html";
}


function pregledSadrzajaApartmana(){
	window.location.href = "dodajNoviSadrzaj.html";
}

function pregledKomentara(){
	window.location.href = "pregledKomentara.html";
}

function pozdravPoruka(korisnik) {
	if (korisnik == undefined) {
		$('#pozdravPor').hide();
	} else {
		$('#pozdravPor').text("Pozdrav " + korisnik.username + " " + korisnik.role);
		$('#pozdravPor').show();
	}
}

function prikazApartmana(korisnik) {
	if (korisnik == undefined) {
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/activeApartments',
			complete: function(data) {
				let apartmani = data.responseJSON;

				let lista = $("#apartmaniTabela tbody");
				lista.empty();
				
				console.log(apartmani.length);
			
           		for(var i = 0; i < apartmani.length;i++) {
					if(apartmani[i].lokacija != null) {
						lista.append("<tr><td>" + i + "</td>"
						+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
						+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija.adresa.naseljenoMesto + "</td>"
						+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
						+ "</td></tr>");
						$("#apartmaniTabela").append(lista);	
					} else {
						lista.append("<tr><td>" + i + "</td>"
						+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
						+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
						+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
						+ "</td></tr>");
						$("#apartmaniTabela").append(lista);	
					}	
				}
			}
		});
	} else if (korisnik.role == 'HOST') {
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/activeApartments',
			complete: function(data) {
				let apartmani = data.responseJSON;

				let lista = $("#apartmaniTabela tbody");
				lista.empty();
				
				console.log(apartmani.length);
				
				apartmaniZaKomentarisanje();

           		for(var i = 0; i < apartmani.length;i++) {
					if(apartmani[i].lokacija != null) {
						lista.append("<tr id='" + apartmani[i].idApartmana + "'><td>" + i + "</td>"
						+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
						+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija.adresa.naseljenoMesto + "</td>"
						+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
						+ "</td>" + "<td> <button id='" + apartmani[i].idApartmana + "'  onclick=window.location.href='novaRezervacija.html?idApartmana=" +apartmani[i].idApartmana + "'> Rezerviši </button></td> <td> <button onclick=pregledKomentara('" + apartmani[i].idApartmana + "')> Komentari </button></td> </tr>");
						$("#apartmaniTabela").append(lista);
					} else {
						lista.append("<tr id='" + apartmani[i].idApartmana + "'><td>" + i + "</td>"
						+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
						+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
						+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
						+ "</td>" + "<td> <button id='" + apartmani[i].idApartmana + "' onclick=window.location.href='novaRezervacija.html?idApartmana=" +apartmani[i].idApartmana + "'> Rezerviši </button></td> <td> <button onclick=pregledKomentara('" + apartmani[i].idApartmana + "')> Komentari</button></td> </tr>");
						$("#apartmaniTabela").append(lista);
					}
				}
			}
		});
	} else if (korisnik.role == 'ADMIN') {
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/all',
			complete: function(data) {
				let apartmani = data.responseJSON;

				let lista = $("#apartmaniTabela tbody");
				lista.empty();

				console.log(apartmani.length);

				//============================================================
				let tip = "<td> Tip </td>";
				let izmena = "<td></td>";
				let brisanje = "<td></td>";
				$("#apartmaniTabela thead tr").append(tip).append(izmena).append(brisanje);
				//==============================================================
           		for(var i = 0; i < apartmani.length;i++){
					   if(apartmani[i].uklonjen == false){
							if(apartmani[i].lokacija != null){
								lista.append("<tr><td>" + i + "</td>"
								+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
								+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija.adresa.naseljenoMesto + "</td>"
								+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
								+ "</td>" + "<td>" + apartmani[i].tip + "</td>" + "<td> <button onClick=window.location.href='podaciApartman.html?idApartmana="+ apartmani[i].idApartmana+ "'> Izmeni </button></td>" +
								"<td> <button id='" + apartmani[i].idApartmana + "'> Obriši </button> </td> </tr>");

								let id = apartmani[i].idApartmana;

								$("#apartmaniTabela").append(lista);
								
								document.getElementById(apartmani[i].idApartmana).onclick =function fun(){
									$.ajax({
										type: 'PUT',
										url: 'rest/apartment/remove/'+ id,
										complete: function(data){

											if(data["status"] == 200){
												window.location.href = "index.html";
											}else if(data["status"] == 500){
												alert("Doslo je do greske!");
											}
										}		
									})
								}

		
							} else {
								lista.append("<tr><td>" + i + "</td>"
								+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
								+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
								+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
								+ "</td>" + "<td>" + apartmani[i].tip + "</td>" + "<td> <button onClick=window.location.href='podaciApartman.html?idApartmana="+ apartmani[i].idApartmana+ "'> Izmeni </button></td>" +
								"<td> <button id='" + apartmani[i].idApartmana + "'> Obriši </button> </td> </tr>");
	
								let id = apartmani[i].idApartmana;
	
								document.getElementById(apartmani[i].idApartmana).onclick =function fun(){
									$.ajax({
										type: 'PUT',
										url: 'rest/apartment/remove/'+ id,
										complete: function(data){
	
											if(data["status"] == 200){
												window.location.href = "index.html";
											}else if(data["status"] == 500){
												alert("Doslo je do greske!");
											}
										}		
									})
								}
	
								$("#apartmaniTabela").append(lista);
							}
					   } else {
							lista.append("<tr style='background-color:red'><td>" + i + "</td>"
							+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
							+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
							+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
							+ "</td>" + "<td>" + apartmani[i].tip + "</td>" + "<td> ------ </td>" +
							"<td> UKLONJEN </td> </tr>");

							$("#apartmaniTabela").append(lista);	
					   }

				}
			}
		});
	} else if (korisnik.role == 'HOST') {
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/hostsApartmants',
			complete: function(data) {
				let apartmani = data.responseJSON;

				let lista = $("#apartmaniTabela tbody");
				lista.empty();
				
				console.log(apartmani.length);
			
           		for(var i = 0; i < apartmani.length;i++){
					let x = apartmani[i].uklonjen;   
					if(!x){
						if(apartmani[i].lokacija != null){
							lista.append("<tr><td>" + i + "</td>"
							+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
							+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija.adresa.naseljenoMesto + "</td>"
							+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
							+ "</td>"  + "<td> <button id='" + apartmani[i].idApartmana + "'> Izmeni </button></td><td><button onclick=pregledKomentara('" + apartmani[i].idApartmana + "')> Komentari </button></td> </tr>");
							
							let id = apartmani[i].idApartmana;

							document.getElementById(apartmani[i].idApartmana).onclick =function fun(){
							//	alert(id);
								window.location.href = "podaciApartman.html?idApartmana="+ id;
							}

							$("#apartmaniTabela").append(lista);
						} else {
							lista.append("<tr><td>" + i + "</td>"
							+ "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
							+ apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
							+ "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
							+ "</td>"  + "<td> <button id='" + apartmani[i].idApartmana + "'> Izmeni </button></td><td><button onclick=pregledKomentara('" + apartmani[i].idApartmana + "')> Komentari </button></td> </tr>");
							
							let id = apartmani[i].idApartmana;

							document.getElementById(apartmani[i].idApartmana).onclick =function fun(){
							//	alert(id);
								window.location.href = "podaciApartman.html?idApartmana="+ id;
							}

							$("#apartmaniTabela").append(lista);
						}
					}
				}
			}
		});
	}
}

function pregledKomentara(idAp){

	window.location.href = "pregledKomentara.html?idApartmana=" + idAp;
}

function apartmaniZaKomentarisanje(){

	$.ajax({
		type: 'GET',
		url: 'rest/review/apartmentsEligibleForReview',
		complete: function(data){
			let sviApartmani = data.responseJSON;

			for(var i = 0; i < sviApartmani.length; i++){
				var id = sviApartmani[i].idApartmana;
				$("#"+ id).append("<td><button onclick=window.location.href='dodajKomentar.html?idApartmana="+ id +  "'> Postavite komentar </button></td>");
			}
		}
	})
}

function filtrirajStatus(){

	var selektovano = $("#filtracijaStatus option:selected").text();
	
	if(selektovano == "ACTIVE"){

	//	alert(selektovano);
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/filter/status/' + selektovano,
			complete: function(data){

				var apartmani = data.responseJSON;

			}
		})

	}else if(selektovano == "INACTIVE"){
	//	alert(selektovano);
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/filter/status/' + selektovano,
			complete: function(data){

				var apartmani = data.responseJSON;

				
			}
		})
	}
}
