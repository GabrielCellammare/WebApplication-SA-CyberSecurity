<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="application.util.entity.Proposal"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%
List<Proposal> propostePrimaCaricamento = (List<Proposal>) request.getAttribute("propostePrimaCaricamento");
%>

<!DOCTYPE html>
<html>

<head>
<title>Homepage Web Application - Proposte progettuali</title>

<style>
<%@include file="/WEB-INF/css/styleIndexLogged.css"%
>
</style>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f4f4;
	margin: 0;
	padding: 0;
}

form {
	max-width: 400px;
	margin: 50px auto;
	background: #fff;
	padding: 20px;
	border-radius: 5px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

table {
	width: 100%;
}

table td {
	padding: 10px;
}

input {
	width: 100%;
	padding: 8px;
	margin-bottom: 10px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	border-radius: 4px;
}

input[type="submit"] {
	background-color: #4caf50;
	color: white;
	cursor: pointer;
}

input[type="submit"]:hover {
	background-color: #45a049;
}

tr.logout-row {
	display: flex;
	justify-content: space-between;
}

input[type="button"].logout-button {
	background-color: red;
	color: white;
	cursor: pointer;
}

input[type="submit"] {
	background-color: #4caf50;
	color: #fff;
	cursor: pointer;
}

#contenutoBanner {
	background-color: #f9f9f9; /* Colore sfondo del banner */
	border: 1px solid #ddd; /* Bordo del banner */
	border-radius: 5px; /* Angoli arrotondati */
	padding: 10px; /* Spaziatura interna */
	margin-top: 20px;
}

#listaProposteBanner {
	background-color: #fff; /* Colore sfondo del banner */
	border: 1px solid #ddd; /* Bordo del banner */
	border-radius: 5px; /* Angoli arrotondati */
	padding: 10px; /* Spaziatura interna */
	margin-top: 20px;
	text-align: center; /* Aggiunto lo stile per centrare il contenuto */
	list-style-type: none; /* Rimuovi i pallini dalla lista */
}
</style>

<script>
    
var isUserLoggedIn = <%=request.getAttribute("login")%>;

if (!isUserLoggedIn || isUserLoggedIn == null) {
    // L'utente non è autenticato, reindirizzalo alla pagina di login
    window.location.href = "userNotLoggedLogin.jsp";
} 
</script>

<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

<script>
	function uploadFile() {
		var formData = new FormData(document.getElementById('uploadForm'));

		$
				.ajax({
					url : 'UploadProposalServlet',
					type : 'POST',
					data : formData,
					processData : false,
					contentType : false,
					success : function(data) {
						// Aggiorna il contenuto del banner con il risultato della risposta AJAX
						$('#bannerContent').html(data);

					},
					error : function(xhr, status, error) {
						console.error(
								'Errore durante il caricamento del file:',
								status, error);
					}
				});
	}

	// Chiamata AJAX separata per caricare la lista delle proposte quando la pagina si carica
	$(document).ready(function() {
		loadProposalsList();
	});

var scrittaAggiunta = false;

var isLogoutExecuted = false;
var isRefreshing = false;
var isProposteLoading = false;

// Funzione per caricare la lista delle proposte
function loadProposalsList() {
    isProposteLoading = true;

    $.ajax({
        url: 'UploadProposalServlet',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            // Aggiorna la lista delle proposte nel banner
            var proposalsList = $('#bannerProposalsList');
            proposalsList.empty();

            // Aggiungo la scritta solo se non è stata ancora aggiunta
            if (!scrittaAggiunta) {
                proposalsList.append('<p><strong>Lista proposte progettuali</strong></p>');
                scrittaAggiunta = true; // Imposta la variabile a true dopo l'aggiunta
            }

            // Itera sulla lista delle proposte
            $.each(data, function (index, proposal) {
                // Utilizza un link anziché un paragrafo e aggiungi una classe per lo stile e l'interattività
                var link = $('<a>', {
                    class: 'file-link',
                    href: '#',
                    'data-nomefile': proposal.fileName,
                    'data-nomeutente': proposal.email,
                    'data-html': proposal.htmlContent,
                    text: 'Email: ' + proposta.username + ', Proposta: ' + proposta.nomeFile
                });
                proposalsList.append($('<li>').append(link));
            });

            
            // Aggiungo uno script jQuery per gestire il clic sul link del file
            $('.file-link').on('click', function (event) {
                event.preventDefault();
                var fileName = $(this).data('fileName');
                var email = $(this).data('email');
                var htmlContent = $(this).data('htmlContent');

                // Creazione del messaggio in grassetto
                var messaggio = "Stai visualizzando la proposta progettuale <strong>" + fileName + "</strong> di <strong>" + email + "</strong>";

                
                // Visualizza il messaggio HTML nel banner
                $('#bannerContent').html('<p>' + messaggio + '</p>' + htmlContent);
            });
            isProposteLoading = false;

        },
        error: function (xhr, status, error) {
            console.error(
                'Errore durante il recupero della lista delle proposte:',
                status, error);
           		isProposteLoading = false;

        }
    });
}

var isLogoutExecuted = false;
var isRefreshing = false;


function logout() {
    if (!isLogoutExecuted) {
        // Effettua una chiamata al server per invalidare la sessione
        $.ajax({
            url: 'LogoutServlet',
            type: 'GET',
            success: function () {
                $('#bannerProposalsList').empty();
                localStorage.removeItem('email');

                // Reindirizza alla pagina di login dopo il logout
                window.location.href = "userNotLoggedLogin.jsp";
            },
            error: function (xhr, status, error) {
                console.error('Errore durante il logout:', status, error);
            }
        });
        isLogoutExecuted = true;
    }
}

//Gestisci il logout quando l'utente sta lasciando la pagina

window.addEventListener('beforeunload', function (event) {
    isRefreshing = true;

    console.log('beforeunload event');

    if (!event.persisted && !isLogoutExecuted && !isRefreshing && !isProposteLoading) {
        // Esegui la funzione di logout solo se necessario e non è un aggiornamento della pagina
                console.log('Performing logout');

        logout();
    }
});

// Gestisci il logout quando l'utente fa clic sul pulsante di navigazione del browser
window.addEventListener('popstate', function () {
    console.log('popstate event');

    if (!isLogoutExecuted && !isRefreshing) {
        // Esegui la funzione di logout solo se necessario
                console.log('Performing logout');

        logout();
    }
});


$(document).ready(function() {
      var inattivitaTimer; // Variabile per memorizzare l'ID del timer

    // Funzione chiamata quando l'utente compie un'azione
    function resetInattivitaTimer() {
        clearTimeout(inattivitaTimer); // Resetta il timer
        inattivitaTimer = setTimeout(function() {
            // Esegui la funzione di logout dopo il periodo di inattività
            logout();
        }, 600000); // 10minuti
    }

    // Chiamata quando la pagina si carica
    $(document).on('click keypress mousemove', function() {
        resetInattivitaTimer();
    });

});


function checkAuthentication() {
    var nomeUtente = "${email}"; // Ottieni il valore dalla JSP
    console.log("Nome utente:", email); // Aggiungi questa riga per stampare nella console

    if (!nomeUtente || nomeUtente.trim() === "") {
        window.location.href = "userNotLoggedLogin.jsp";
    }
}

// Chiamata alla funzione durante il caricamento della pagina
document.addEventListener("DOMContentLoaded", function() {
    checkAuthentication();
    loadProposalsList(); // Aggiunta la chiamata alla funzione loadProposalsList
    
    // Quando la pagina è completamente caricata
    var bannerContent = $('#bannerContent');
    if (bannerContent.is(':empty')) {
        bannerContent.html('<p>Non stai visualizzando nessuna proposta</p>');
    }
});


</script>

</head>
<body>
	<div class="navbar">
		<h1>Proposte progettuali</h1>
		<div class="navbar-buttons">
			<a href="logout.jsp" class="button">Logout</a>
		</div>
	</div>
	<div class="container">
		<!-- Form di caricamento proposta -->
	<form id="uploadForm" method="post" action="javascript:void(0);"
		enctype="multipart/form-data" onsubmit="uploadFile()">
		<input type="hidden" name="nomeUtente" value="${nomeUtente}" />
		<table>
			<tr>
				<h1>Benvenuto, ${nomeUtente}!</h1>
			</tr>
			<tr>
				<td>Carica proposta progettuale</td>
				<td><input type="file" name="Proposta progettuale"
					autocomplete="off"></td>

			</tr>
			<tr>
				<td></td>
				<td><small>Il file deve essere di formato .txt e pesare
						massimo 20 MB.</small></td>
			</tr>
			<tr>
				<td><input type="button" class="logout-button" value="Logout"
					onclick="logout()"></td>
				<td><input type="submit" value="Carica Proposta"></td>
			</tr>
			<tr>
				<!-- Banner -->
				<!--<div id="banner">-->

					<div id="contenutoBanner">
						<%=request.getAttribute("contenutoFiltrato")%>
					</div>
				<!-- </div> -->

			</tr>
			<tr>

				<div id="listaProposteBanner"></div>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>