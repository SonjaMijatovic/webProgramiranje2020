$(document).ready(function(){

    document.getElementById("pretraga").hidden = true;
    document.getElementById("tipNijeOdabran").hidden = true;
    document.getElementById("brSobaPrazan").hidden = true;
    document.getElementById("brOsobaPrazan").hidden = true;

    let tipValidan = false;
    let sobeValidne = false;
    let osobeValidne = false;
    

    $("#pretragaBtn").click(function(event){

        if($("#pretraga").is(":visible")){
            document.getElementById("pretraga").hidden = true;
        }else {
            document.getElementById("pretraga").hidden = false;
        }
    })

    $("#pretrazi").click(function(event){

        event.preventDefault();

        let tip = $("#tip option:selected").val();
        let sobe = $("#brSoba").val();
        let osobe = $("#brOsoba").val();

        if(tip == "nista"){
            document.getElementById("tipNijeOdabran").hidden = false;
            tipValidan = false;
        }else {
            document.getElementById("tipNijeOdabran").hidden = true;
            tipValidan = true;
        }

        if(sobe == "" || sobe == " "){
            document.getElementById("brSobaPrazan").hidden = false;
            sobeValidne = false;
        }else {
            document.getElementById("brSobaPrazan").hidden = true;
            sobeValidne = true;
        }

        if(osobe == "" || osobe == " "){
            document.getElementById("brOsobaPrazan").hidden = false;
            osobeValidne = false;
        }else {
            document.getElementById("brOsobaPrazan").hidden = true;
            osobeValidne = true;
        }

        if(tipValidan && sobeValidne && osobeValidne){
            
            $.ajax({
                type: 'GET',
                url: 'rest/user/getUser',
                complete: function(data){

                    let korisnik = data.responseJSON;

                    if(korisnik == undefined){
                        $.ajax({
                            type: 'GET',
                            url: 'rest/apartment/search/'+ tip + '/'+ sobe + '/'+ osobe + '/'+ true,
                            complete: function(data){
            
                                let apartmani = data.responseJSON;
            
                                let lista = $("#apartmaniTabela tbody");
                                lista.empty();
            
                                for(var i = 0; i < apartmani.length;i++){
                                    lista.append("<tr><td>" + i + "</td>"
                                    + "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
                                    + apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
                                    + "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
                                    + "</td>");
                                    $("#apartmaniTabela").append(lista);        
                                }
                            }
                        })
                    } else if(korisnik.uloga == "ADMIN"){
                        $.ajax({
                            type: 'GET',
                            url: 'rest/apartment/search/'+ tip +'/'+ sobe +'/'+ osobe + '/' + false,
                            complete: function(data){   

                                let apartmani = data.responseJSON;

                                let lista = $("#apartmaniTabela tbody");
                                lista.empty();

                                console.log(apartmani.length);

                                for(var i = 0; i < apartmani.length;i++){
                                    if(apartmani[i].uklonjen == false){
                                        lista.append("<tr><td>" + i + "</td>"
                                        + "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
                                        + apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
                                        + "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
                                        + "</td>" + "<td>" + apartmani[i].tip + "</td>" + "<td><a id='izmeni' href='podaciApartman.html'> Izmeni</a> </td>" +
                                        "<td> <button id='" + apartmani[i].idApartmana + "'> Obriši </button> </td> </tr>");
            
                                        let id = apartmani[i].idApartmana;
            
                                        document.getElementById(apartmani[i].idApartmana).onclick =function fun(){
                                            $.ajax({
                                                type: 'PUT',
                                                url: 'rest/apartment/remove/'+ id,
                                                complete: function(data){
            
                                                    if(data["status"] == 200){
                                                        window.location.href = "index.html";
                                                    }else if(data["status"] == 500){
                                                        alert("Doslo je do greske!");
                                                    }
                                                }		
                                            })
                                        }
                                        $("#apartmaniTabela").append(lista);	
                                   } else {
                                        lista.append("<tr style='background-color:red'><td>" + i + "</td>"
                                        + "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
                                        + apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
                                        + "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
                                        + "</td>" + "<td>" + apartmani[i].tip + "</td>" + "<td> ------ </td>" +
                                        "<td> UKLONJEN </td> </tr>");
            
                                        $("#apartmaniTabela").append(lista);	
                                   }
                                }
                            }
                        })
                    }else if(korisnik.uloga == "GUEST"){
                        $.ajax({
                            type: 'GET',
                            url: 'rest/apartment/search/'+ tip + '/'+ sobe + '/'+ osobe + '/' + true,
                            complete: function(data){
            
                                let apartmani = data.responseJSON;
            
                                let lista = $("#apartmaniTabela tbody");
                                lista.empty();
            
                                for(var i = 0; i < apartmani.length;i++){
                                    lista.append("<tr><td>" + i + "</td>"
                                    + "<td>" + apartmani[i].brSoba + "</td> " + "<td>" 
                                    + apartmani[i].brGostiju + "</td>" + "<td>" + apartmani[i].lokacija + "</td>"
                                    + "<td>" + apartmani[i].domacin + "</td>" + "<td>" + apartmani[i].cenaPoNoci
                                    + "</td>");
                                    $("#apartmaniTabela").append(lista);        
                                }
                            }
                        });
                    } else if(korisnik.uloga == "Domacin"){
                    }
                }
            })
        }
    })
});