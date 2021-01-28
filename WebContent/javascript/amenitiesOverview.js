$(document).ready(function(){

    document.getElementById("idPoruka").hidden = true;
    document.getElementById("nazivPoruka").hidden = true;
    document.getElementById("tipPoruka").hidden = true;

    $("#odustani").click(function(event){
		event.preventDefault();
        window.location.href = "index.html";
    })

    $.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data){
			getAmenities(data.responseJSON);
		}		
	})

    $("#dodajStavku").click(function(event){
        event.preventDefault();

        let idStavke = document.getElementById("IdStavke").value;
        let nazivStavke = document.getElementById("nazivStavke").value;
        let tipStavke = document.getElementById("tipStavke").value;

        let idDobar = false;
        let nazivDobar = false;
        let tipDobar = false;

        if(idStavke == "" || idStavke == " "){
            document.getElementById("idPoruka").hidden = false;
            idDobar = false;
        }else {
            document.getElementById("idPoruka").hidden = true;
            idDobar = true;
        }

        if(nazivStavke == "" || nazivStavke == " "){
            document.getElementById("nazivPoruka").hidden = false;
            nazivDobar = false;
        }else {
            document.getElementById("nazivPoruka").hidden = true;
            nazivDobar = true;
        }
        
        if(tipStavke == "" || tipStavke == " "){
            document.getElementById("tipPoruka").hidden = false;
            tipDobar = false;
        }else {
            document.getElementById("tipPoruka").hidden = true;
            tipDobar = true;
        }

        if(nazivDobar == true && idDobar == true){
            let podaci = { 
                "id": $("#IdStavke").val(),
                "name": $("#nazivStavke").val(),
                "type": $("#tipStavke").val()
            }

            var s = JSON.stringify(podaci);
            $.ajax({
                type: 'POST',
                url: 'rest/amenities/add',
                data: s,
                contentType: 'application/json',
                dataType: 'json',
                complete: function(data){
                    if(data["status"] == 200){
                        alert("Amenity successfully added!");
                        window.location.href = "amenitiesOverview.html";
                    }else if(data["status"] == 500)
                        alert("Amenity with selected ID already exists!");
                    else
                        alert("Error!");
                }
            })
        }
    })
});

function funkcija(id){
    window.location.href = "izmenaStavkeSadrzaja.html?id="+ id;
}

function getAmenities(korisnik){
    if (korisnik == undefined ) {
        alert("No content!");
        window.location.href = "index.html";
    } else if (korisnik.role == 'GUEST') {
    	alert("No content!");
        window.location.href = "index.html";
    } else if (korisnik.role == 'HOST') {
    	alert("No content!");
        window.location.href = "index.html";
    } else if (korisnik.role == 'ADMIN') {
        $.ajax({
            type: 'GET',
            url: 'rest/amenities/all',
            complete: function(data){
                let savSadrzaj = data.responseJSON;
                let lista = $("#sadrzajApartmana tbody");
                lista.empty();
                for (var i = 0; i < savSadrzaj.length; i++) {
                    if(savSadrzaj[i].deleted == false) {
                        lista.append(
                        		"<tr><td>"+ savSadrzaj[i].id + "</td><td>" + savSadrzaj[i].name + "</td><td>" + savSadrzaj[i].type + "</td><td>" +
                        "<button onclick='funkcija("+ savSadrzaj[i].id + "); return false;'> Edit </button></td><td><button id='" + savSadrzaj[i].id + "'> Delete </button></td></tr>");
    
                        let id1 = savSadrzaj[i].id;
                        document.getElementById(savSadrzaj[i].id).onclick = function fun(){
                            $.ajax({
                                type: 'PUT',
                                url: 'rest/amenities/remove/'+ id1,
                                complete: function(data){
                                    
                                    if(data["status"] == 200) {
                                        alert("Successfully removed.");
                                        window.location.href = "amenitiesOverview.html";
                                    } else {
                                        alert("Error!");
                                    }
                                }
                            });
                        }
                        $("#sadrzajApartmana").append(lista);
                        
                    } else if(savSadrzaj[i].deleted == true) {
                        lista.append("<tr style='background-color:red'><td>"+ savSadrzaj[i].id + "</td><td>"+ savSadrzaj[i].name + "</td>"+
                        "<td>------</td><td> Removed </td></tr>");
                        $("#sadrzajApartmana").append(lista);
                    }
                }
            }
        });
    }
}
