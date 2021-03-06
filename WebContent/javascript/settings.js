$(document).ready(function () {
	$('#pozdravPor').hide();

	$.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function (data) {
			izloguj();
			pozdravPoruka(data.responseJSON);
			sakrijDugmad(data.responseJSON);
			dodatneOpcije(data.responseJSON);
			prikazApartmana(data.responseJSON);
		}
	})
});

function sakrijDugmad(korisnik) {
	if (korisnik == undefined) {
		$("#login_buttons").show();
		$("#acc_buttons").hide();
	} else {
		$("#login_buttons").hide();
		$("#acc_buttons").show();
	}
}

function dodatneOpcije(korisnik) {

	if (korisnik == undefined)
		return;

	if (korisnik.role == "ADMIN") {
		$("#acc_buttons").append("<button type='submit' id='korisnici_Btn' onclick=usersOverview()>Users Overview </button> <br/>");
		$("#acc_buttons").append("<button type='submit' id='sadrzaj_Btn' onclick=amenitiesOverview()> Amenities Overview </button><br/>");
		$("#acc_buttons").append("<button type='submit' id='rezervacije_Btn' onclick=reservationOverview()> Reservation Overview </button><br/>");
		$("#acc_buttons").append("<button type='submit' id='komentari_Btn' onclick=reviewOverview()> Reviews Overview </button> <br/>");
	} else if (korisnik.role == "GUEST") {
		$("#acc_buttons").append("<button type='submit' id='rezervacije' onclick=reservationOverview()> My reservations </button> <br/>");
	} else if (korisnik.role == "HOST") {
		$("#acc_buttons").append("<button type='submit' id='dodajApp_Btn' onclick=addApartment()>Add apartment </button> <br/>");
		$("#acc_buttons").append("<button type='submit' id='rezervacije_Btn' onclick=reservationOverview()> My reservations </button><br/>");
	}
}

function izloguj() {
	$("#odjava_btn").click(function (event) {
		event.preventDefault();

		$.ajax({
			type: 'POST',
			url: 'rest/user/logout',
			complete: function (data) {
				window.location.href = "login.html";
			}
		})
	});

	$("#nalog_btn").click(function (event) {
		event.preventDefault();

		window.location.href = "account.html";
	});
}

function reservationOverview() {
	window.location.href = "reservationOverview.html";
}

function usersOverview() {
	window.location.href = "usersOverview.html";
}

function addApartment() {
	window.location.href = "addApartment.html";
}

function amenitiesOverview() {
	window.location.href = "amenitiesOverview.html";
}

function reviewOverview() {
	window.location.href = "reviewOverview.html";
}

function pozdravPoruka(korisnik) {
	if (korisnik == undefined) {
		$('#pozdravPor').hide();
	} else {
		$('#pozdravPor').text("Hello " + korisnik.role.toLowerCase() + " " + korisnik.username);
		$('#pozdravPor').show();
	}
}

function prikazApartmana(korisnik) {
	if (korisnik == undefined) {
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/activeApartments',
			complete: function (data) {
				let apartmani = data.responseJSON;
				let lista = $("#apartmaniTabela tbody");
				lista.empty();
				for (var i = 0; i < apartmani.length; i++) {
					if (apartmani[i].location != null) {
						lista.append("<tr><td>" + i + "</td>" +
							"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
							apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location.address.city + "</td>" +
							"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
							"</td></tr>");
						$("#apartmaniTabela").append(lista);
					} else {
						lista.append("<tr><td>" + i + "</td>" +
							"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
							apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location + "</td>" +
							"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
							"</td></tr>");
						$("#apartmaniTabela").append(lista);
					}
				}
			}
		});
	} else if (korisnik.role == 'GUEST') {
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/activeApartments',
			complete: function (data) {
				let apartmani = data.responseJSON;
				let lista = $("#apartmaniTabela tbody");
				lista.empty();

				apartmaniZaKomentarisanje();

				for (var i = 0; i < apartmani.length; i++) {
					if (apartmani[i].location != null) {
						lista.append("<tr id='" + apartmani[i].id + "'><td>" + i + "</td>" +
							"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
							apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location.address.city + "</td>" +
							"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
							"</td>" + "<td> <button id='" + apartmani[i].id + "'onclick=window.location.href='newReservation.html?idApartmana=" + apartmani[i].id + "'> Make reservation </button></td> <td> <button onclick=reviewOverviewByApartment('" + apartmani[i].id + "')> Reviews </button></td> </tr>");
						$("#apartmaniTabela").append(lista);
					} else {
						lista.append("<tr id='" + apartmani[i].id + "'><td>" + i + "</td>" +
							"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
							apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location + "</td>" +
							"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
							"</td>" + "<td> <button id='" + apartmani[i].id + "' onclick=window.location.href='newReservation.html?idApartmana=" + apartmani[i].id + "'> Make reservation </button></td> <td> <button onclick=reviewOverviewByApartment('" + apartmani[i].id + "')> Reviews </button></td> </tr>");
						$("#apartmaniTabela").append(lista);
					}
				}
			}
		});
	} else if (korisnik.role == 'ADMIN') {
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/all',
			complete: function (data) {
				let apartmani = data.responseJSON;

				let lista = $("#apartmaniTabela tbody");
				lista.empty();

				let tip = "<td> Type </td>";
				let izmena = "<td></td>";
				let brisanje = "<td></td>";
				$("#apartmaniTabela thead tr").append(tip).append(izmena).append(brisanje);
				for (var i = 0; i < apartmani.length; i++) {
					if (apartmani[i].deleted == false) {
						if (apartmani[i].location != null) {
							lista.append("<tr><td>" + i + "</td>" +
								"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
								apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location.address.city + "</td>" +
								"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
								"</td>" + "<td>" + apartmani[i].type + "</td>" + "<td> <button onClick=window.location.href='apartmentOverview.html?idApartmana=" + apartmani[i].id + "'> Edit </button></td>" +
								"<td> <button id='" + apartmani[i].id + "'> Delete </button> </td> </tr>");

							let id = apartmani[i].id;

							$("#apartmaniTabela").append(lista);

							document.getElementById(apartmani[i].id).onclick = function fun() {
								$.ajax({
									type: 'PUT',
									url: 'rest/apartment/remove/' + id,
									complete: function (data) {

										if (data["status"] == 200) {
											window.location.href = "index.html";
										} else if (data["status"] == 500) {
											alert("Error removing apartment!");
										}
									}
								})
							}

						} else {
							lista.append("<tr><td>" + i + "</td>" +
								"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
								apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location + "</td>" +
								"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
								"</td>" + "<td>" + apartmani[i].type + "</td>" + "<td> <button onClick=window.location.href='apartmentOverview.html?idApartmana=" + apartmani[i].id + "'> Edit </button></td>" +
								"<td> <button id='" + apartmani[i].id + "'> Delete </button> </td> </tr>");

							let id = apartmani[i].id;

							document.getElementById(apartmani[i].id).onclick = function fun() {
								$.ajax({
									type: 'PUT',
									url: 'rest/apartment/remove/' + id,
									complete: function (data) {
										if (data["status"] == 200) {
											window.location.href = "index.html";
										} else if (data["status"] == 500) {
											alert("Error removing apartment!");
										}
									}
								})
							}

							$("#apartmaniTabela").append(lista);
						}
					} else {
						lista.append("<tr style='background-color:#ED7458'><td>" + i + "</td>" +
							"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
							apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location + "</td>" +
							"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
							"</td>" + "<td>" + apartmani[i].type + "</td>" + "<td> ------ </td>" +
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
			complete: function (data) {
				let apartmani = data.responseJSON;

				let lista = $("#apartmaniTabela tbody");
				lista.empty();

				console.log(apartmani.length);

				for (var i = 0; i < apartmani.length; i++) {
					let x = apartmani[i].uklonjen;
					if (!x) {
						if (apartmani[i].location != null) {
							lista.append("<tr><td>" + i + "</td>" +
								"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
								apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location.address.city + "</td>" +
								"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
								"</td>" + "<td> <button id='" + apartmani[i].id + "'> Edit </button></td><td><button onclick=reviewOverviewByApartment('" + apartmani[i].id + "')> Reviews </button></td> </tr>");

							let id = apartmani[i].id;

							document.getElementById(apartmani[i].id).onclick = function fun() {
								// alert(id);
								window.location.href = "apartmentOverview.html?idApartmana=" + id;
							}

							$("#apartmaniTabela").append(lista);
						} else {
							lista.append("<tr><td>" + i + "</td>" +
								"<td>" + apartmani[i].numberOfRooms + "</td> " + "<td>" +
								apartmani[i].numberOfGuests + "</td>" + "<td>" + apartmani[i].location + "</td>" +
								"<td>" + apartmani[i].hostUsername + "</td>" + "<td>" + apartmani[i].price +
								"</td>" + "<td> <button id='" + apartmani[i].id + "'> Edit </button></td><td><button onclick=reviewOverviewByApartment('" + apartmani[i].id + "')> Reviews </button></td> </tr>");

							let id = apartmani[i].id;

							document.getElementById(apartmani[i].id).onclick = function fun() {
								// alert(id);
								window.location.href = "apartmentOverview.html?idApartmana=" + id;
							}

							$("#apartmaniTabela").append(lista);
						}
					}
				}
			}
		});
	}
}


function reviewOverviewByApartment(idAp) {
	window.location.href = "reviewOverview.html?idApartmana=" + idAp;
}

function apartmaniZaKomentarisanje() {

	$.ajax({
		type: 'GET',
		url: 'rest/review/apartmentsEligibleForReview',
		complete: function (data) {
			let sviApartmani = data.responseJSON;

			for (var i = 0; i < sviApartmani.length; i++) {
				var id = sviApartmani[i].id;
				$("#" + id).append("<td><button onclick=window.location.href='addReview.html?idApartmana=" + id + "'> Tel us your review </button></td>");
			}
		}
	})
}

function filtrirajStatus() {

	var selektovano = $("#filtracijaStatus option:selected").text();

	if (selektovano == "ACTIVE") {

		// alert(selektovano);
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/filter/status/' + selektovano,
			complete: function (data) {

				var apartmani = data.responseJSON;

			}
		})

	} else if (selektovano == "INACTIVE") {
		// alert(selektovano);
		$.ajax({
			type: 'GET',
			url: 'rest/apartment/filter/status/' + selektovano,
			complete: function (data) {
				var apartmani = data.responseJSON;
			}
		})
	}
}