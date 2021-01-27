$(document).ready(function(){

    //nova sifra skriveno
    document.getElementById("lab3").hidden = true;
    document.getElementById("novaLoz").hidden = true;
    document.getElementById("lab4").hidden = true;
    document.getElementById("novaLoz2").hidden = true;

    document.getElementById("imeP").hidden = true;
    document.getElementById("przP").hidden = true;
    document.getElementById("novaP").hidden = true;
    document.getElementById("ponoviP").hidden = true;

    let imeIspravno = false;
    let przIspravno = false;
    let prvaLozIspravna = false;
    let drugaLozIspravna = false;

    $.ajax({
        type: 'GET',
        url: 'rest/user/getUser',
        complete: function(data){

            let korisnik = data.responseJSON;

            let uloga = "<td><label><b><i>" + korisnik.role + "</i></b></label></td>";
            $("#u").append(uloga);
            
            let tabela = $("#nalogTabela");

            $("#korIme:text").val(korisnik.username);
            $("#staraLoz").val(korisnik.password);
            $("#ime").val(korisnik.firstname);
            $("#prz").val(korisnik.lastname);
            
            $("#izmeni").click(function(event){
                event.preventDefault();
            
                if($("#lab3").is(":visible")){
                    document.getElementById("lab3").hidden = true;
                    document.getElementById("novaLoz").hidden = true;
                    document.getElementById("lab4").hidden = true;
                    document.getElementById("novaLoz2").hidden = true;
                }else {
                    document.getElementById("lab3").hidden = false;
                    document.getElementById("novaLoz").hidden = false;
                    document.getElementById("lab4").hidden = false;
                    document.getElementById("novaLoz2").hidden = false;
                }
            })

            $("#odustani").click(function(event){
                event.preventDefault();

                window.location.href = "index.html";
            })

            $("#sacuvaj").click(function(event){
                event.preventDefault();

                if($("#ime").val() == "" || $("#ime").val() == " "){
                    document.getElementById("imeP").hidden = false;
                }else {
                    document.getElementById("imeP").hidden = true;
                    imeIspravno = true;
                }

                if($("#prz").val() == "" || $("#prz").val() == " "){
                    document.getElementById("przP").hidden = false;
                }else {
                    document.getElementById("przP").hidden = true;
                    przIspravno = true; 
                }

                let duzinaLozinke = $("#novaLoz").val().length;
                let vrPrva = $("#novaLoz").val();
                let vrDruga = $("#novaLoz2").val();

                if($("#novaLoz").is(":visible")){

                    if(duzinaLozinke < 5){
                        document.getElementById("novaP").hidden = false;
                        prvaLozIspravna = false;
                    }else{
                        document.getElementById("novaP").hidden = true;
                        prvaLozIspravna = true;
                    }
                    if(vrPrva !== vrDruga){
                        document.getElementById("ponoviP").hidden = false;
                        drugaLozIspravna = false;
                    }else{
                        document.getElementById("ponoviP").hidden = true;
                        drugaLozIspravna = true;
                    }

                    if(imeIspravno == true && przIspravno == true && prvaLozIspravna == true && drugaLozIspravna == true){
                        let korImeStaro = $("#korIme").val();
                        let novaLoz = $("#novaLoz").val();
                        let novoIme = $("#ime").val();
                        let novoPrz = $("#prz").val();
                        let novPol = $("#pol option:selected").val();

                        $.ajax({
                            type: 'PUT',
                            url: 'rest/user/edit/'+ korImeStaro + '/' + novaLoz + '/'+ novoIme + '/'+ novoPrz + '/'+ novPol,
                            complete: function(data){

                                let korisnik = data.responseJSON;

                                if(korisnik != null){
                                    alert("User updated.");
                                    window.location.href = "index.html";
                                }else {
                                    alert("Error while updating user!");
                                }
                            }
                        })
                    }

                } else {
                    document.getElementById("novaP").hidden = true;
                    document.getElementById("ponoviP").hidden = true;
                    prvaLozIspravna = true;
                    drugaLozIspravna = true;

                    let korImeStaro = $("#korIme").val();
                    let staraSifra = $("#staraLoz").val();
                    let novoIme = $("#ime").val();
                    let novoPrz = $("#prz").val();
                    let novPol = $("#pol option:selected").val();

                    if(imeIspravno == true && przIspravno == true){
                        $.ajax({
                            type: 'PUT',
                            url: 'rest/user/edit/'+ korImeStaro + '/'+ staraSifra + '/'+ novoIme + '/'+ novoPrz+ '/'+ novPol,
                            complete: function(data){

                                let korisnik = data.responseJSON;

                                if(korisnik != null){
                                    alert("User updated.");
                                    window.location.href = "index.html";
                                }else {
                                    alert("Error while updating user!");
                                }
                            }
                        })
                    }
                }
            })
        }     
    })
});
