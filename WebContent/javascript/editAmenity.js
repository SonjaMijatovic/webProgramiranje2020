$(document).ready(function(){

    var idStavke = getUrlVars()["id"];

    document.getElementById("idStavke").innerHTML = idStavke;

    $("#odustani").click(function(event){
        event.preventDefault();
        window.location.href = "amenitiesOverview.html";
    })

    $.ajax({
        type: 'GET',
        url: 'rest/amenities/'+ idStavke,
        complete: function(data){
            let stavka = data.responseJSON;
            $("#nazivStavke:text").val(stavka.name);
        }
    })

    $("#prihvati").click(function(event){
        event.preventDefault();

        let podaci = {
            "id": idStavke,
            "name": $("#nazivStavke").val()
        }

        var d = JSON.stringify(podaci);

        $.ajax({
            type: 'PUT',
            url: 'rest/amenities/edit/'+ idStavke,
            data: d,
            contentType: 'application/json',
            dataType: 'json',
            complete: function(data){
                if (data["status"] == 200) {
                    alert("Amenity changed.");
                    window.location.href = "amenitiesOverview.html";
                } else {
                    alert("Error!");
                }
            }
        })
    })

    $.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data){
			checkRole(data.responseJSON);
		}		
	})
});

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function checkRole(korisnik){
    if(korisnik == undefined){
        alert("No content!");
        window.location.href = "index.html";

    }else if(korisnik.uloga == 'GUEST'){
        alert("No content!");
        window.location.href = "index.html";
       
    }else if(korisnik.uloga == 'HOST'){
        alert("No content!");
        window.location.href = "index.html";
    }
}