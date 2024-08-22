<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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

<style><%@ include file ="/WEB-INF/css/styleIndexLogged.css"%></style>

<script>
    
var isUserLoggedIn = <%=request.getAttribute("login")%>;

if (!isUserLoggedIn || isUserLoggedIn == null) {
    // L'utente non � autenticato, reindirizzalo alla pagina di login
    window.location.href = "login.jsp";
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

            // Aggiungo la scritta solo se non � stata ancora aggiunta
            if (!scrittaAggiunta) {
                proposalsList.append('<p><strong>Lista proposte progettuali</strong></p>');
                scrittaAggiunta = true; // Imposta la variabile a true dopo l'aggiunta
            }

            // Itera sulla lista delle proposte
            $.each(data, function (index, proposal) {
                // Utilizza un link anzich� un paragrafo e aggiungi una classe per lo stile e l'interattivit�
                var link = $('<a>', {
                    class: 'file-link',
                    href: '#',
                    'data-nomefile': proposal.nomeFile,
                    'data-nomeutente': proposal.username,
                    'data-html': proposal.contenutoHtml,
                    text: 'Username: ' + proposta.username + ', Proposta: ' + proposta.nomeFile
                });
                proposalsList.append($('<li>').append(link));
            });

            
            // Aggiungo uno script jQuery per gestire il clic sul link del file
            $('.file-link').on('click', function (event) {
                event.preventDefault();
                var nomeFile = $(this).data('nomefile');
                var nomeUtente = $(this).data('nomeutente');
                var contenutoHtml = $(this).data('html');

                // Creazione del messaggio in grassetto
                var messaggio = "Stai visualizzando la proposta progettuale <strong>" + nomeFile + "</strong> di <strong>" + nomeUtente + "</strong>";

                
                // Visualizza il messaggio HTML nel banner
                $('#bannerContent').html('<p>' + messaggio + '</p>' + contenutoHtml);
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
                localStorage.removeItem('nomeUtente');

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
        // Esegui la funzione di logout solo se necessario e non � un aggiornamento della pagina
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
            // Esegui la funzione di logout dopo il periodo di inattivit�
            logout();
        }, 600000); // 10minuti
    }

    // Chiamata quando la pagina si carica
    $(document).on('click keypress mousemove', function() {
        resetInattivitaTimer();
    });

});


function checkAuthentication() {
    var nomeUtente = "${nomeUtente}"; // Ottieni il valore dalla JSP
    console.log("Nome utente:", nomeUtente); // Aggiungi questa riga per stampare nella console

    if (!nomeUtente || nomeUtente.trim() === "") {
        window.location.href = "userNotLoggedLogin.jsp";
    }
}

// Chiamata alla funzione durante il caricamento della pagina
document.addEventListener("DOMContentLoaded", function() {
    checkAuthentication();
    loadProposalsList(); // Aggiunta la chiamata alla funzione loadProposalsList
    
    // Quando la pagina � completamente caricata
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
			<a href="caricaProposta.jsp" class="button">Carica Proposta</a> <a
				href="logout.jsp" class="button">Logout</a>
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
					<td><small>Il file deve essere di formato .txt e
							pesare massimo 20 MB.</small></td>
				</tr>
				<tr>
					<td><input type="button" class="logout-button" value="Logout"
						onclick="logout()"></td>
					<td><input type="submit" value="Carica Proposta"></td>
				</tr>
				<tr>
					<!-- Banner -->
					<!--<div id="banner">-->

					<div id="bannerContent">
						<%=request.getAttribute("contenutoFiltrato")%>
					</div>
					<!-- </div> -->

				</tr>
				<tr>

					<div id="bannerProposalsList"></div>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
