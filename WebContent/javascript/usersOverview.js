$(document).ready(function(){
    document.getElementById("pretraga").hidden = true;

    $.ajax({
		type: 'GET',
		url: 'rest/user/getUser',
		complete: function(data){
			odrediKorisnika(data.responseJSON);
		}		
    })
    
    $("#prikazPretrage").click(function(event){
        event.preventDefault();

        if($("#pretraga").is(":visible"))
            document.getElementById("pretraga").hidden = true;
        else 
        document.getElementById("pretraga").hidden = false; 
    })

    // if field is empty
    var tag = document.createElement("p");
    tag.setAttribute("id", "emptyUsername");
    let text = document.createTextNode("Field can't be empty.");
    tag.append(text);	
    tag.style.color = 'red';
    let obojeniTekst = tag;
    document.getElementById("k").after(obojeniTekst);
    document.getElementById("emptyUsername").hidden = true;

    // if user doesn't exist
    let tag2 = document.createElement("p");
    tag2.setAttribute("id", "usernameNotExisting");
    let text2 = document.createTextNode("User doesn't exist.");
    tag2.append(text2);	
    tag2.style.color = 'red';
    let obojeniTekst2 = tag2;
    document.getElementById("k").after(obojeniTekst2);
    document.getElementById("usernameNotExisting").hidden = true;

    $("#pretrazi").click(function(event) {
        event.preventDefault();

        let korIme = $("#korImePretraga").val();
        let polKor = $("#pol option:selected" ).text();
        let uloga = $("#uloga option:selected").text();

        $.ajax({
            type: 'GET',
            url: 'rest/user/search/'+ korIme + '/' + uloga + '/' + polKor,
            complete: function(data){
                let korisnik = data.responseJSON;

                if(korisnik == null){
                    if (korIme == "") {
                        document.getElementById("emptyUsername").hidden = false;
                        document.getElementById("usernameNotExisting").hidden = true;
                    } else {
                        document.getElementById("usernameNotExisting").hidden = false;
                        document.getElementById("premptyUsernamevi").hidden = true;
                    }
                } else {
                    document.getElementById("usernameNotExisting").hidden = true;
                    document.getElementById("emptyUsername").hidden = true;

                    let lista = $("#korisniciTabela tbody");
                    lista.empty();

                    if (korisnik[0].role == "ADMIN"){
                        lista.append("<tr><td>" + korisnik[0].username + "</td>"
                        + "<td>" + korisnik[0].password + "</td> " + "<td>" 
                        + korisnik[0].firstname + "</td>" + "<td>" + korisnik[0].lastname + "</td>"
                        + "<td>" + korisnik[0].gender + "</td>" + "<td>" + korisnik[0].role 
                        + "</td>" + "<td></td>" );
                        $("#korisniciTabela").append(lista);

                    } else if (korisnik[0].uloga == "HOST"){
                        lista.append("<tr><td>" + korisnik[0].username + "</td>"
                        + "<td>" + korisnik[0].password + "</td> " + "<td>" 
                        + korisnik[0].firstname + "</td>" + "<td>" + korisnik[0].lastname + "</td>"
                        + "<td>" + korisnik[0].gender + "</td>" + "<td>" + korisnik[0].role 
                        + "</td>" + "<td></td>" );
                        $("#korisniciTabela").append(lista);

                    } else {
                        lista.append("<tr><td>" + korisnik[0].username + "</td>"
                        + "<td>" + korisnik[0].password + "</td> " + "<td>" 
                        + korisnik[0].firstname + "</td>" + "<td>" + korisnik[0].lastname + "</td>"
                        + "<td>" + korisnik[0].gender + "</td>" + "<td>" + korisnik[0].role 
                        + "</td>" + "<td><button id='" + korisnik[0].username + "'> Update role </button></td>" );
                        
                        let korisnicko = korisnik[0].username;

                        document.getElementById(korisnik[0].username).onclick = function fun(){
                            $.ajax({
                                type: 'PUT',
                                url: 'rest/user/updateRole/'+ korisnicko,
                                complete: function(data){
                                    if(data["status"] == 200){
                                        window.location.href = "usersOverview.html";
                                    }else if(data["status"] == 500){
                                        alert("Error!");
                                    }
                                }
                            });
                        }
                        $("#korisniciTabela").append(lista); 
                    }
                }
            }
        });     
    })

    $("#ponisti").click(function(event) {
        event.preventDefault();
        $.ajax({
            type: 'GET',
            url: 'rest/user/getAllUsers',
            complete: function(data) {
    
                let korisnici = data.responseJSON;
                let lista = $("#korisniciTabela tbody");
                lista.empty();
    
                console.log(korisnici.length);
    
                for (var i = 0; i < korisnici.length;i++) {
                    if (korisnici[i].role == "ADMIN") {
                         lista.append("<tr><td>" + korisnici[i].username + "</td>"
                       + "<td>" + korisnici[i].password + "</td> " + "<td>" 
                       + korisnici[i].firstname + "</td>" + "<td>" + korisnici[i].lastname + "</td>"
                       + "<td>" + korisnici[i].gender + "</td>" + "<td>" + korisnici[i].role 
                       + "</td>" + "<td></td>" );
                    $("#korisniciTabela").append(lista);

                    } else if(korisnici[i].role == "HOST") {
                        lista.append("<tr><td>" + korisnici[i].username + "</td>"
                        + "<td>" + korisnici[i].password + "</td> " + "<td>" 
                        + korisnici[i].firstname + "</td>" + "<td>" + korisnici[i].lastname + "</td>"
                        + "<td>" + korisnici[i].gender + "</td>" + "<td>" + korisnici[i].role 
                        + "</td>" + "<td></td>" );
                     $("#korisniciTabela").append(lista);

                    } else { 
                         lista.append("<tr><td>" + korisnici[i].username + "</td>"
                        + "<td>" + korisnici[i].password + "</td> " + "<td>" 
                        + korisnici[i].firstname + "</td>" + "<td>" + korisnici[i].lastname + "</td>"
                        + "<td>" + korisnici[i].gender + "</td>" + "<td>" + korisnici[i].role 
                        + "</td>" + "<td><button id='"+ korisnici[i].username + "'> Update role </button></td>");
                        
                        let korisnicko = korisnici[i].username;
    
                        document.getElementById(korisnici[i].username).onclick = function fun() {
                            $.ajax({
                                type: 'PUT',
                                url: 'rest/user/updateRole/'+ korisnicko,
                                complete: function(data){
                                    if (data["status"] == 200) {
                                        window.location.href = "usersOverview.html";
                                    } else if (data["status"] == 500) {
                                        alert("Error!");
                                    }
                                }
                            });
                        }
                        $("#korisniciTabela").append(lista);
                    }
                }
            }
        });
    })
});

function odrediKorisnika(korisnik) {
    if(korisnik == undefined){
        alert("Nedostupan sadrzaj!");
        window.location.href = "index.html";

    } else if(korisnik.role == 'GUEST') {
        alert("Nedostupan sadrzaj!");
        window.location.href = "index.html";

    } else if(korisnik.role == 'ADMIN') {
        $.ajax({
            type: 'GET',
            url: 'rest/user/getAllUsers',
            complete: function(data){
                let korisnici = data.responseJSON;
                let lista = $("#korisniciTabela tbody");
                lista.empty();
    
                console.log(korisnici.length);
    
                for (var i = 0; i < korisnici.length;i++) {
                    if (korisnici[i].role == "ADMIN") {
                         lista.append("<tr><td>" + korisnici[i].username + "</td>"
                       + "<td>" + korisnici[i].password + "</td> " + "<td>" 
                       + korisnici[i].firstname + "</td>" + "<td>" + korisnici[i].lastname + "</td>"
                       + "<td>" + korisnici[i].gender + "</td>" + "<td>" + korisnici[i].role 
                       + "</td>" + "<td></td>" );
                    $("#korisniciTabela").append(lista);

                    } else if(korisnici[i].role == "HOST") {
                        lista.append("<tr><td>" + korisnici[i].username + "</td>"
                        + "<td>" + korisnici[i].password + "</td> " + "<td>" 
                        + korisnici[i].firstname + "</td>" + "<td>" + korisnici[i].lastname + "</td>"
                        + "<td>" + korisnici[i].gender + "</td>" + "<td>" + korisnici[i].role 
                        + "</td>" + "<td></td>" );
                     $("#korisniciTabela").append(lista);

                    } else { 
                         lista.append("<tr><td>" + korisnici[i].username + "</td>"
                        + "<td>" + korisnici[i].password + "</td> " + "<td>" 
                        + korisnici[i].firstname + "</td>" + "<td>" + korisnici[i].lastname + "</td>"
                        + "<td>" + korisnici[i].gender + "</td>" + "<td>" + korisnici[i].role 
                        + "</td>" + "<td><button id='"+ korisnici[i].username + "'> Update role </button></td>");
                        
                        let korisnicko = korisnici[i].username;
    
                        document.getElementById(korisnici[i].username).onclick = function fun() {
                            $.ajax({
                                type: 'PUT',
                                url: 'rest/user/updateRole/'+ korisnicko,
                                complete: function(data) {
                                    if (data["status"] == 200) {
                                        window.location.href = "usersOverview.html";
                                    } else if(data["status"] == 500) {
                                        alert("Error!");
                                    }
                                }
                            });
                        }
                        $("#korisniciTabela").append(lista);
                    }
                }
            }
        })
    } else if(korisnik.role == "HOST") {
        alert("No content!");
        window.location.href = "index.html";
    }
}
