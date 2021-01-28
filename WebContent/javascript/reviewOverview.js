$(document).ready(function(){
    $.ajax({
        type: 'GET',
        url: 'rest/user/getUser',
        complete: function(data){
            var number = getUrlVars()["idApartmana"];
            loadBasedOnRole(data.responseJSON);
            if (number != null) {
            	ucitajCekiraniStatus(number);
            }
        }
    })
});

function loadBasedOnRole(korisnik) {
    if(korisnik == undefined){
        alert("No content!");
        window.location.href = "index.html";

    } else if(korisnik.role == 'GUEST'){
        var number = getUrlVars()["idApartmana"];
        prikazKomentaraGost(number);

    } else if(korisnik.role == 'ADMIN'){
        prikazKomentaraAdmin();
    
    } else if(korisnik.role == 'HOST'){
        var number = getUrlVars()["idApartmana"];
        prikazKomentaraDomacin(number);
    }
}

function prikazKomentaraAdmin(){
    $.ajax({
        type: 'GET',
        url: 'rest/review/all',
        complete: function(data){
            let sviKomentari = data.responseJSON;
           document.getElementById("formaKomentari").reset();
           
            for(var i = 0; i < sviKomentari.length; i++) {
                var newDiv = document.createElement("div"); 
                newDiv.innerHTML = "<br/> Review written by: <b>" + sviKomentari[i].guestId + "</b><br/> Apartment: <a href='showApartment.html?idApartmana=" + sviKomentari[i].apartmentId + "'> Show apartment </a> <br/><br/>  " + sviKomentari[i].text + 
                    "<br/><br/> Rate by user:<b> " + sviKomentari[i].rate + "</b><br/><br/>"; 
                $("#formaKomentari").append(newDiv);
            }
        }
    })
}

function prikazKomentaraGost(idAp){
    $.ajax({
        type: 'GET',
        url: 'rest/review/reviews/'+ idAp,
        complete: function(data){
            let sviKomentari = data.responseJSON;
            document.getElementById("formaKomentari").reset();
            
            for(var i = 0; i < sviKomentari.length; i++) {
                if (sviKomentari[i].visible == true){
                    var newDiv = document.createElement("div"); 
                    newDiv.innerHTML = "<br/> Review written by: <b>" + sviKomentari[i].guestId + "</b><br/> " + sviKomentari[i].text + 
                        "<br/><br/> Rate by user:<b> " + sviKomentari[i].rate + "</b><br/><br/>"; 
                    $("#formaKomentari").append(newDiv);
                }
            }
        }
    })
}

function prikazKomentaraDomacin(idAp){
    $.ajax({
        type: 'GET',
        url: 'rest/review/getKomentareMogApartmana/'+ idAp,
        complete: function(data){

            let sviKomentari = data.responseJSON;

            document.getElementById("formaKomentari").reset();
            
            for(var i = 0; i < sviKomentari.length; i++){
                var newDiv = document.createElement("div"); 
                newDiv.innerHTML = "Review written by: <b>" + sviKomentari[i].guestId + "</b><br/><br/>  " + sviKomentari[i].text + 
                    "<br/><br/> Rate by user: <b> " + sviKomentari[i].rate + "</b><br/><br/> <label for='" + sviKomentari[i].id + "'>Vidljiv komentar</label><input type='checkbox' styleId='" + sviKomentari[i].text + "' id='"+ sviKomentari[i].id +"' onclick=sakrijKomentar('" + sviKomentari[i].id + "')><br/>"; 
                $("#formaKomentari").append(newDiv);
            }
        }
    })
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function sakrijKomentar(idKom){
    $.ajax({
        type: 'PUT',
        url: 'rest/review/changeVisibility/' + idKom,
        complete: function(data){
            if(data["status"] == 200){
                alert("Visibility set!");
            }else {
                alert("Visibility was not set!");
            }
        }
    })
}

function ucitajCekiraniStatus(idAp){
    $.ajax({
        type: 'GET',
        url: 'rest/review/reviews/'+ idAp,
        complete: function(data) {
            let komentariApartmana = data.responseJSON;

            for (var i = 0; i < komentariApartmana.length; i++) {
                if (komentariApartmana[i].visible == true) {
                    let a = document.getElementById(komentariApartmana[i].idKomentara);
                    a.checked = true;
                } else if(komentariApartmana[i].komentarVidljiv == false) {
                    let a = document.getElementById(komentariApartmana[i].idKomentara);
                    a.checked = false;
                }
            }
        }
    })
}