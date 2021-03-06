var savSadrzajApartmana = new Array();
var cekiraniSadrzaj = new Array();
var podaciSadrzaj = [];

var podaciAdresa = {};
var podaciLokacija = {};

$(document).ready(function() {

    $('#PozPorApp').hide();

    $("#odustani").click(function(event){
        event.preventDefault();
        window.location.href = "index.html";
    })

    $.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data){
            pozdravPorukaApp(data.responseJSON);
            $('#dodajApp').click(function(event) {
                event.preventDefault();
                dodajApartman(data.responseJSON);

                let sobe = $('#brSoba').val();
                let osobe = $('#brGostiju').val();
                let cena =  $('#cena').val();
            });
        }	
    });
    
    ucitajSadrzajApartmana();

    document.getElementById("greskaBrSobaPor").hidden = true;
    document.getElementById("greskaBrGostijuPor").hidden = true;
    document.getElementById("greskaCenaPor").hidden = true;
    document.getElementById("greskaUlica").hidden = true;
    document.getElementById("greskaMesto").hidden = true;
    document.getElementById("greskaPostanskiBroj").hidden = true;

    
    let ulicaIspravna = false;
    let mestoIspravno = false;
    let postanskiBrIspravan = false;
});

function pozdravPorukaApp(korisnik) {
	if (korisnik == undefined) {
		$('#pozPorApp').hide();
	} else {
		$('#pozPorApp').text("Hello " + korisnik.role.toLowerCase() + " " + korisnik.username);
		$('#pozPorApp').show();
	}
}

function dodajApartman(korisnik) {
//    alert($('#datumZaIzdOD').val());
//    alert($('#datumZaIzdDO').val());
    var datumOD = new Date($('#datumZaIzdOD').val());
    var datumDO = new Date($('#datumZaIzdDO').val());

    formirajAdresu();

        let geoSirina = document.getElementById("geoSirina").textContent;
        let geoDuzina = document.getElementById("geoDuzina").textContent;

        podaciLokacija = {
            "latitude" : geoSirina,
            "longitude" : geoDuzina,
            "address" : podaciAdresa
        }
    
    postavljanjeSadrzaja();
    
    var datumiZaIzdavanjeList = getDates(datumOD, datumDO);
    let podaci  = {
        "numberOfRooms": $('#brSoba').val(),
        "numberOfGuests": $('#brGostiju').val(),
        "type": $('#tip option:selected').text(),
        "price": $('#cena').val(),
        "image": $('#blah').val(),
        "hostUsername": korisnik.username,
        "datesForRenting": datumiZaIzdavanjeList,
        "availabilityPerDates": datumiZaIzdavanjeList,
        "location": podaciLokacija,
        "amenities": podaciSadrzaj
    }
    
    var korIme = korisnik.username;

    let s = JSON.stringify(podaci);

    $.ajax ({
        url: 'rest/apartment/add',
        type: 'POST',
        data: s,
        contentType: 'application/json',
        dataType: 'json',
        complete: function(data) {
            if (data["status"] == 200) {
                alert("Apartment successfuly added.");
                window.location.href = "index.html";
            } else {
                alert("Apartment was not added.");
            }
        }
    });
} 

//iskorisceno u metodi ispod(ucitajSadrzajApartmana())
function ucitajCekiranSadrzaj(id){
    if (document.getElementById(id).checked) {
        cekiraniSadrzaj.push(id);
    } 
    if(!document.getElementById(id).checked) {
        var index = $.inArray(id,cekiraniSadrzaj);
        if(index != -1){
            cekiraniSadrzaj.splice(index, 1);
            alert("cekiraniSadrzaj " + cekiraniSadrzaj);
        }
    }
}

//prilikom klika dodavanja
function postavljanjeSadrzaja(){
	var savSadrzaj = savSadrzajApartmana;
	for(var i = 0; i < savSadrzaj.length; i++){
        for(var j = 0; j < cekiraniSadrzaj.length; j++){
            if(cekiraniSadrzaj[j] == savSadrzaj[i].id){
                jednaStavka  = { 
                                "id": savSadrzaj[i].id,
                                "name": savSadrzaj[i].name,
                                "type": savSadrzaj[i].type,
                                "deleted": false
                                }
                podaciSadrzaj.push(jednaStavka);
            }
        }
    }
}

function ucitajSadrzajApartmana(){
    $.ajax({
        type: 'GET',
        url: 'rest/amenities/all',
        complete: function(data) {

            let savSadrzaj = data.responseJSON;
            savSadrzajApartmana = savSadrzaj;
            let lista = $("#tabelaSadrzaj tbody");
            lista.empty();

            for(var i = 0; i < savSadrzaj.length; i++){
                if(savSadrzaj[i].deleted == false){
                    lista.append("<tr><td><input type='checkbox' onclick=ucitajCekiranSadrzaj('"+ savSadrzaj[i].id + "') id='" + savSadrzaj[i].id +"'>" + 
                    "<label for='"+ savSadrzaj[i].id + "'>"+ savSadrzaj[i].name + "</label></td></tr>");
                    $("#tabelaSadrzaj").append(lista);
                }
            }
        }
    })
}

function readURL(input){
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#blah')
                .attr('src', e.target.result)
                .width(200)
                .height(200);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

Date.prototype.addDays = function(days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
}

function getDates(startDate, stopDate) {
    var dateArray = new Array();
    var currentDate = startDate;
    while (currentDate <= stopDate) {
        dateArray.push(new Date (currentDate));
        currentDate = currentDate.addDays(1);
    }
    return dateArray;
}

function formirajAdresu(){

    let ulicaBroj = $("#ulica").val();
    let mesto = $("#mesto").val();
    let postanskiBroj = $("#postanskiBroj").val();

    podaciAdresa = {
            "streetAndNumber": ulicaBroj,
            "city": mesto,
            "postalCode": postanskiBroj
        }
}

function sirinaDuzinaFunkcija(){

    var precision1 = 1000000; // 2 decimals
    var randomnum1 = Math.floor(Math.random() * (100 * precision1 - 1 * precision1) + 1 * precision1) / (1*precision1);

    var precision2 = 1000000; // 2 decimals
    var randomnum2 = Math.floor(Math.random() * (100 * precision2 - 1 * precision2) + 1 * precision2) / (1*precision2);

    document.getElementById('geoSirina').innerHTML =  "<b>"+ randomnum1 + "</b>";
    document.getElementById('geoDuzina').innerHTML = "<b>"+ randomnum2 + "</b>";
}


