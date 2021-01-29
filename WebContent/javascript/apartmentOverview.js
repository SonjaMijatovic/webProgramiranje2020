var postojeciSadrzaj = Array();
var cekiraniSadrzaj = new Array();
var podaciSadrzaj = [];

var podaciAdresa = {};
var podaciLokacija = {};

var pomocna = [];

$(document).ready(function(){

    var number = getUrlVars()["idApartmana"];

    $.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data){
			checkRole(data.responseJSON);
		}		
	})

    document.getElementById("greskaBrSobaPor").hidden = true;
    document.getElementById("greskaBrGostijuPor").hidden = true;
    document.getElementById("greskaCenaPor").hidden = true;
    document.getElementById("greskaUlica").hidden = true;
    document.getElementById("greskaMesto").hidden = true;
    document.getElementById("greskaPostanskiBroj").hidden = true;

    let brSobaIspravan = false;
    let brGostijuIspravan = false;
    let cenaIspravna = false;

    let ulicaIspravna = false;
    let mestoIspravno = false;
    let postanskiBrIspravan = false;

	$.ajax({
		type: 'GET',
		url: 'rest/apartment/'+ number,
		complete: function(data){
			
            let apartman = data.responseJSON;
            var domacinApp = apartman.hostUsername;
            postojeciSadrzaj = apartman.amenities;
            
            let tip = "<td> <b> <i>" + apartman.type + "</i></b></td>";
            $("#k").append(tip);

            $("#brSoba:text").val(apartman.numberOfRooms);
            $("#brGostiju:text").val(apartman.numberOfGuests);
            $("#cena:text").val(apartman.price);
            //$("#komentar:text").val(apartman.komentar);
            document.getElementById("statusLabela").innerHTML = "<b>" + apartman.status + "</b>";
            $("#ulica:text").val(apartman.location.address.streetAndNumber);
            $("#mesto:text").val(apartman.location.address.city);
            $("#postanskiBroj").val(apartman.location.address.postalCode);
            document.getElementById("geoSirina").innerHTML = "<b>" + apartman.location.latitude + "</b>";
            document.getElementById("geoDuzina").innerHTML = "<b>" + apartman.location.longitude + "</b>";

            let statusApp = document.getElementById("statusLabela").textContent;

            $("#promeniStatusBtn").click(function(event){
                event.preventDefault();
        
                if( document.getElementById("statusLabela").textContent == "Active"){
                    document.getElementById("statusLabela").innerHTML = "<b>Inactive</b>";
                }
                else if(document.getElementById("statusLabela").textContent == "Inactive"){
                    document.getElementById("statusLabela").innerHTML = "<b>Active</b>";
                }
            })
            $("#izmeniApp").click(function(event){
                event.preventDefault();

                izmeniApartman(data.responseJSON, number);
            });
        }
    });
	
    ucitajSadrzajApartmana();

    $("#odustani").click(function(event){
        event.preventDefault();

        window.location.href = "index.html";
    });

});

function izmeniApartman(apartman, number) {
    formirajAdresu();

    let geoSirina = document.getElementById("geoSirina").textContent;
    let geoDuzina = document.getElementById("geoDuzina").textContent;

    podaciLokacija = {
        "latitude" : geoSirina,
        "longitude" : geoDuzina,
        "address" : podaciAdresa
    }    
   var ulicaBroj =  document.getElementById("ulica").value;

    if(ulicaBroj == "" || ulicaBroj == " "){
        document.getElementById("greskaUlica").hidden = false;
        ulicaIspravna = false;
    }else {
        document.getElementById("greskaUlica").hidden = true;
        ulicaIspravna = true;
    }

    let mesto = document.getElementById("mesto").value;

    if(mesto == "" || mesto == " "){
        document.getElementById("greskaMesto").hidden = false;
        mestoIspravno = false;
    }else {
        document.getElementById("greskaMesto").hidden = true;
        mestoIspravno = true;
    }

    let postBr = document.getElementById("postanskiBroj").value;

    if(postBr == "" || postBr == " "){
        document.getElementById("greskaPostanskiBroj").hidden = false;
        postanskiBrIspravan = false;
    }else {
        document.getElementById("greskaPostanskiBroj").hidden = true;
        postanskiBrIspravan = true;
    }

    let statusApartmana = document.getElementById("statusLabela").textContent;
    let brSobaApartmana = document.getElementById("brSoba").value;
    let brGostijuApartmana = document.getElementById("brGostiju").value;
    let cenaApartmana = document.getElementById("cena").value;

    if(brSobaApartmana == "" || brSobaApartmana == " "){
    document.getElementById("greskaBrSobaPor").hidden = false;
    brSobaIspravan = false;
    }else {
    document.getElementById("greskaBrSobaPor").hidden = true;
    brSobaIspravan = true;
    }

    if(brGostijuApartmana == "" || brGostijuApartmana == " "){
        document.getElementById("greskaBrGostijuPor").hidden = false;
        brGostijuIspravan = false;
    }else {
        document.getElementById("greskaBrGostijuPor").hidden = true;
        brGostijuIspravan = true;
    }

    if(cenaApartmana == "" || cenaApartmana == " "){
        document.getElementById("greskaCenaPor").hidden = false;
        cenaIspravna = false;
    }else {
        document.getElementById("greskaCenaPor").hidden = true;
        cenaIspravna = true;
    }

    postavljanjeSadrzaja();
   // alert(podaciSadrzaj + "------ ovo su podaci izvan funkcije!");
   // alert(apartman.domacin + "Ovog domacina saljem");

    if(brGostijuIspravan == true && brSobaIspravan == true && cenaIspravna == true && ulicaIspravna == true && mestoIspravno == true && postanskiBrIspravan == true){

    podaciZaSlanje = { 
            "type": apartman.type,
//            "to": apartman.datumiZaIzdavanje,
//            "to": 1657942,
//            "from": apartman.dostupnostPoDatumima,
//            "from": 1657893,
            "reservations": apartman.reservations,
            "id": number,
            "hostUsername": apartman.hostUsername,
            "status": statusApartmana,
            "numberOfRooms": brSobaApartmana,
            "numberOfGuests": brGostijuApartmana,
            "price": cenaApartmana,
            "location": podaciLokacija,
            "amenities": podaciSadrzaj
        }

        var d = JSON.stringify(podaciZaSlanje);

        $.ajax({
            type: 'PUT',
            url: 'rest/apartment/edit/' + number,
            data: d,
            contentType: 'application/json',
            dataType: 'json',
            complete: function(data){
                if(data["status"] == 200){
                    window.location.href = "index.html";
                }else {
                    alert("Apartman neuspesno izmenjen!");
                }
            }
        });
    }   
}

//fja za uzimanje parametra iz url-a koji smo prethodno poslali
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

//ucitavanje slike
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

function ucitajSadrzajApartmana(){

    $.ajax({
        type: 'GET',
        url: 'rest/amenities/all',
        complete: function(data){

            let savSadrzaj = data.responseJSON;

            let lista = $("#tabelaSadrzaj tbody");
            lista.empty();
            
//            for(var i = 0; i < savSadrzaj.length; i++){
//                if(savSadrzaj[i].uklonjen == false){
//                    lista.append("<tr><td><input type='checkbox' onclick=ucitajCekiranSadrzaj('"+ savSadrzaj[i].id + "') id='" + savSadrzaj[i].id +"'>" + 
//                                        "<label for='"+ savSadrzaj[i].id + "'>"+ savSadrzaj[i].item + "</label></td></tr>");
//                    $("#tabelaSadrzaj").append(lista);
//                }
//            }
            
            for (var i = 0; i < savSadrzaj.length; i++) {
                if (savSadrzaj[i].deleted == false) {
                	for (var j = 0; j < postojeciSadrzaj.length; j++) {
                		if (postojeciSadrzaj[j].id == savSadrzaj[i].id) {
                	       	lista.append("<tr><td><input type='checkbox' checked onclick=ucitajCekiranSadrzaj('"+ savSadrzaj[i].id + "') id='" + savSadrzaj[i].id +"'>" + 
                                    "<label for='"+ savSadrzaj[i].id + "'>"+ savSadrzaj[i].name + "</label></td></tr>");
                		}
                	}
                	lista.append("<tr><td><input type='checkbox' onclick=ucitajCekiranSadrzaj('"+ savSadrzaj[i].id + "') id='" + savSadrzaj[i].id +"'>" + 
                            "<label for='"+ savSadrzaj[i].id + "'>"+ savSadrzaj[i].name + "</label></td></tr>");
                	$("#tabelaSadrzaj").append(lista);
                }
            }
        }
    });
}



function ucitajCekiranSadrzaj(id){

    if(document.getElementById(id).checked){
        cekiraniSadrzaj.push(id);
     //   alert(id);
    }if(!document.getElementById(id).checked){
        var index = $.inArray(id,cekiraniSadrzaj);
        if(index != -1){
            cekiraniSadrzaj.splice(index, 1);
         //   alert(cekiraniSadrzaj);
        }
    }
}

function postavljanjeSadrzaja(){

    $.ajax({
        type: 'GET',
        url: 'rest/amenities/all',
        complete: function(data){

            let savSadrzaj = data.responseJSON;

            for(var i = 0; i < savSadrzaj.length; i++){
                for(var j = 0; j < cekiraniSadrzaj.length; j++){
                    if(cekiraniSadrzaj[j] == savSadrzaj[i].id){
                      //  alert(cekiraniSadrzaj[j] + "PRONADJEN!");
                        jednaStavka  = { 
                                        "id": savSadrzaj[i].id,
                                        "name": savSadrzaj[i].item,
                                         "deleted": false
                                        }
                        podaciSadrzaj.push(jednaStavka);
                        alert("Trenutno postavljen sadrzaj u funkciji: " + podaciSadrzaj);
                    }
                }
            }
        }
    })
}

function checkRole(korisnik){
    if(korisnik == undefined){
        alert("No content!");
        window.location.href = "index.html";

    }else if(korisnik.role == 'GUEST'){
        alert("No content!");
        window.location.href = "index.html";
    }
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

