$(document).ready(function(){

    $("#odustani").click(function(event){
        event.preventDefault();

        window.location.href = "index.html";
    })

    $.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data){
			nabaviApartman(data.responseJSON);
		}		
	})
});

function nabaviApartman(korisnik){

    if(korisnik == undefined){
        alert("No content!");
        window.location.href = "index.html";

    } else if (korisnik.role == 'GUEST') {
        var number = getUrlVars()["idApartmana"];

//        prikaziSadrzaj(number);

        $.ajax({
            type: 'GET',
            url: 'rest/apartment/'+ number,
            complete: function(data){
                let apartman = data.responseJSON;
                alert(apartmen.hostUsername);
                document.getElementById("domacin").innerHTML = "<b>" + apartman.hostUsername + "</b>";
//                $('#dostupniDatumi').text(apartman.dostupnostPoDatumima);
                $('#potvrdi').click(function(event){
                    event.preventDefault();
                    posaljiRezervaciju(data.responseJSON,korisnik);
                })
            }
        })


    }else if(korisnik.role == 'ADMIN'){
        alert("No content!");
        window.location.href = "index.html";

    }else if(korisnik.role == 'HOST'){
        alert("No content!");
        window.location.href = "index.html";
    }
}

function posaljiRezervaciju(apartman,korisnik) {   
    var datumPocetkaRez = new Date($('#datumRezervacije').val());
    var datumPocetni = JSON.stringify(datumPocetkaRez);
  //  alert(datumPocetni);
    let brNocenja = $('#brNocenja').val();
    let datumKrajaRez = datumPocetkaRez.addDays(parseInt(brNocenja, 10));
  //  alert("ALOOOOOOOOO: " + datumPocetkaRez);
    let datumPocetkaRezString = (JSON.stringify(datumPocetkaRez)).substr(1,10);
 //   alert(datumPocetkaRezString)
  //  alert(brNocenja);
    //alert(datumKrajaRez);
    let listaRezervisanih = getDates(datumPocetkaRez, datumKrajaRez);
  //  alert(listaRezervisanih);

   // alert(listaRezervisanihString);
    let novalista = [];
     for(var i = 0; i < listaRezervisanih.length; i++){
        novalista[i] = (JSON.stringify(listaRezervisanih[i])).substr(1,10);
    //    alert(novalista[i]);
    } 

    let poruka = $("#poruka").val();
    let ukupnaCena = apartman.price * brNocenja;
  //  alert("Ukupna cena!!!! : " + ukupnaCena);
    
    var n = true;

    //alert(novalista);


    for(var i = 0; i < novalista.length; i++){
        if (!(apartman.dostupnostPoDatumima).includes(novalista[i])) {
            n = false;
            break;
        }
    } 
    // "listaRezervisanihDatuma": listaRezervisanih,
   // alert(n);
   // alert(korisnik.korisnicko_ime);
    if (n == true) {
        let podaci = {
            "datumiRezervacije": listaRezervisanih,
            "apartman": apartman.id,
            "pocetniDatum": datumPocetkaRez,
            "brNocenja": brNocenja,
            "ukCena": ukupnaCena,
            "poruka": message,
            "gost": korisnik.username
        }
    
        var s = JSON.stringify(podaci);
    
        $.ajax ({
            url: 'rest/reservation/add/' + apartman.id,
            type: 'POST',
            data: s,
            contentType: 'application/json',
            dataType: 'json',
            complete: function(data) {
                if (data["status"] == 200) {
                    alert("Reservation created.");
                    window.location.href = "index.html";
                } else {
                    alert("Reservation was not successful!");
                }
            }
        });
    } else {
        alert("Dates are not available!");
    }
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
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

function prikaziSadrzaj(apartman){

    $.ajax({
        type: 'GET',
        url: 'rest/amenities/' + apartman,
        complete: function(data){

            var sadrzaj = data.responseJSON;

            for(var i = 0; i < sadrzaj.length; i++){
               $("#sadrzajRed").append("<tr><td> <label> <i>" + sadrzaj[i].name + "</i></label></td></tr>");
            }
        }
    })
}



