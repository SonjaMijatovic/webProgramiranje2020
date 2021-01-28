$(document).ready(function(){

    $.ajax({
        type: 'GET',
        url: 'rest/user/getUser',
        complete: function(data){
        	loadBasedOnRole(data.responseJSON);
        }
    })

    $("#povratak").click(function(event){
        event.preventDefault();
        window.location.href = "reviewOverview.html";
    })    
});

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function loadBasedOnRole(korisnik){
    if(korisnik == undefined){
        alert("Nedostupan sadrzaj!");
        window.location.href = "index.html";
    }else if(korisnik.role == "HOST"){
        alert("Nedostupan sadrzaj!");
        window.location.href = "index.html";
    }else if(korisnik.role == "GUEST"){
        alert("Nedostupan sadrzaj!");
        window.location.href = "index.html";
    }else if(korisnik.role == "ADMIN"){
        var number = getUrlVars()["idApartmana"];
        prikaziApartman(number);
    }
}

function prikaziApartman(idAp){
    prikaziSadrzaj(idAp);
    $.ajax({
        type: 'GET',
        url: 'rest/apartment/' + idAp,
        complete: function(data){

            let apartman = data.responseJSON;

            $("#status").text(apartman.status);
            $("#tip").text(apartman.type);
            $("#domacin").text(apartman.hostUsername);
            $("#brSoba").text(apartman.numberOfRooms);
            $("#brGostiju").text(apartman.numberOfGuests);
            $("#cenaPoNoci").text(apartman.price);
            $("#ulicaIBroj").text(apartman.location.address.streetAndNumber);
            $("#naseljenoMesto").text(apartman.location.address.city);
            $("#postanskiBr").text(apartman.location.address.postalCode);
        }
    })
}


function prikaziSadrzaj(idAp){

    $.ajax({
        type: 'GET',
        url: 'rest/amenities/apartment/' + idAp,
        complete: function(data){

            var sadrzaj = data.responseJSON;

            for(var i = 0; i < sadrzaj.length; i++){
               $("#tabelaSadrzaj tbody").append("<tr><td> <label> <i>" + sadrzaj[i].name + "</i></label></td></tr>");
            }
        }
    })
}

