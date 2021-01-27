
var idRez = new Array();
var idAp = new Array();

$(document).ready(function(){
	
	$.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data) {
			proveriKorisnika(data.responseJSON);
		}
	})
});

function proveriKorisnika(korisnik){
	if (korisnik == undefined) {
		alert("No content!");
		window.location.href = "index.html";

	} else if (korisnik.role == "GUEST") {
		$("#naslov").text("My reservations");

		let mojeRezervacije = korisnik.reservations;
		
		let lista = $("#rezervacijeTabela tbody");
		lista.empty();

		let datumPocetka = "<td> <b> Start date </b> </td>";
		let ukupnaCena = "<td> <b> Total price </b> </td>";
		let domacin = "<td> <b> Host </b> </td>";
		let status = "<td> <b> Reservation status </b> </td>";

		$("#rezervacijeTabela thead tr").append(datumPocetka).append(ukupnaCena).append(domacin).append(status);

		for(var i = 0; i < mojeRezervacije.length; i++){
			let epoxDate = sveRezervacije[i].startDate;					
			let startDate = new Date(sveRezervacije[i].startDate * 1000);
			startDate = new Intl.DateTimeFormat('en-GB').format(startDate);
			
			if(mojeRezervacije[i].status == "CREATED" || mojeRezervacije[i].status == "ACCEPTED"){
				lista.append("<tr><td>"+ stvarnoSad + "</td><td>" + mojeRezervacije[i].totalPrice + "</td><td>" + "</td><td>" +  mojeRezervacije[i].status +"</td><td><button id='" + mojeRezervacije[i].id + "' onclick=odustanakRezervacija('" + mojeRezervacije[i].id + "','" + mojeRezervacije[i].apartmentId + "')> Cancel </button></td></tr>");
				$("#rezervacijeTabela").append(lista);
				
			} else if(mojeRezervacije[i].status == "CANCELED"){
				lista.append("<tr><td>"+ stvarnoSad + "</td><td>" + mojeRezervacije[i].totalPrice + "</td><td>" + "</td><td>" +  mojeRezervacije[i].status +"</td></tr>");
				$("#rezervacijeTabela").append(lista);
				
			} else if (mojeRezervacije[i].status == "REJECTED" || mojeRezervacije[i].status == "COMPLETED"){
				lista.append("<tr><td>"+ stvarnoSad + "</td><td>" + mojeRezervacije[i].totalPrice + "</td><td>" + "</td><td>" +  mojeRezervacije[i].status +"</td></tr>");
				$("#rezervacijeTabela").append(lista);
			}
		}

	} else if(korisnik.role == "HOST") {
		$("#naslov").text("Reservations of my apartments");

		let lista = $("#rezervacijeTabela tbody");
		lista.empty();

		let gost = "<td> <b> Reserved by </b></td>";
		let datumPocetka = "<td><b> Start date </b></td>";
		let ukupnaCena = "<td><b> Total price <b></td>";
		let status = "<td> <b> Reservation status <b> </td>";
		$("#rezervacijeTabela thead tr").append(gost).append(datumPocetka).append(ukupnaCena).append(status);

		$.ajax({
			type: 'GET',
			url: 'rest/reservations/hostsReservations',
			complete: function(data){

				let mojeRezervacije = data.responseJSON;
				
				for(var i = 0; i < mojeRezervacije.length; i++){
					
					if (mojeRezervacije[i].status == "Kreirana") {
						let epoxDate = sveRezervacije[i].startDate;					
						let startDate = new Date(sveRezervacije[i].startDate * 1000);
						startDate = new Intl.DateTimeFormat('en-GB').format(startDate);
						lista.append("<tr><td>" + mojeRezervacije[i].gost + "</td><td>"+ stvarnoSad + "</td><td>" + mojeRezervacije[i].ukCena +  "</td><td>"+ mojeRezervacije[i].status +"</td><td>" +
							 		"<button onclick=prihvatiRezervaciju('" + mojeRezervacije[i].idRezervacije + "','" + mojeRezervacije[i].apartman + "')> Prihvati </button></td><td><button onclick=odbijRezervaciju('"  + mojeRezervacije[i].idRezervacije + "','" + mojeRezervacije[i].apartman + "')> Odbij </button></td></tr>");				
						$("#rezervacijeTabela").append(lista);


					} else if(mojeRezervacije[i].status == "ACCEPTED") {
						let epoxDate = sveRezervacije[i].startDate;					
						let startDate = new Date(sveRezervacije[i].startDate * 1000);
						startDate = new Intl.DateTimeFormat('en-GB').format(startDate);
						lista.append("<tr><td>" + mojeRezervacije[i].gost + "</td><td>"+ stvarnoSad + "</td><td>" + mojeRezervacije[i].ukCena +  "</td><td>"+ mojeRezervacije[i].status +"</td>"+
											"<td><button onclick=zavrsenaRezervacija('" + mojeRezervacije[i].idRezervacije + "','" + mojeRezervacije[i].apartman + "')> Završi </button></td><td><button onclick=odbijRezervaciju('"  + mojeRezervacije[i].idRezervacije + "','" + mojeRezervacije[i].apartman + "')> Odbij </button></td></tr>");
						$("#rezervacijeTabela").append(lista);

					} else if (mojeRezervacije[i].status == "CANCELED" || mojeRezervacije[i].status == "REJECTED" || mojeRezervacije[i].status == "COMPLETED"){
						let epoxDate = sveRezervacije[i].startDate;					
						let startDate = new Date(sveRezervacije[i].startDate * 1000);
						startDate = new Intl.DateTimeFormat('en-GB').format(startDate);
						lista.append("<tr><td>" + mojeRezervacije[i].gost + "</td><td>"+ stvarnoSad + "</td><td>" + mojeRezervacije[i].ukCena +  "</td><td>"+ mojeRezervacije[i].status +"</td><tr>");
						$("#rezervacijeTabela").append(lista);
					}
				}
			}
		})

	} else if (korisnik.role == "ADMIN") {
		$("#naslov").text("Overview of all reservations in the system");

		let lista = $("#rezervacijeTabela tbody");
		lista.empty();

		let gost = "<td> <b> Rezervation made by guest </b></td>";
		let domacin = "<td> <b> Host <b> </td>";
		let datumPocetka = "<td><b> Start date </b></td>";
		let ukupnaCena = "<td><b> Total price <b></td>";
		let status = "<td> <b> Status <b> </td>";
		$("#rezervacijeTabela thead tr").append(gost).append(domacin).append(datumPocetka).append(ukupnaCena).append(status);

		$.ajax({
			type: 'GET',
			url: 'rest/reservation/all',
			complete: function(data){

				let sveRezervacije = data.responseJSON;

				for(var i = 0; i < sveRezervacije.length; i++) {
					let epoxDate = sveRezervacije[i].startDate;					
					let startDate = new Date(sveRezervacije[i].startDate * 1000);
					startDate = new Intl.DateTimeFormat('en-GB').format(startDate);
					lista.append("<tr><td>" + sveRezervacije[i].guestId + "</td><td>" + "</td><td>"+ startDate + "</td><td>" + sveRezervacije[i].totalPrice +  "</td><td>"+ sveRezervacije[i].status +"</td></tr>");
					$("#rezervacijeTabela").append(lista);
				}
			}
		})
	}
}
