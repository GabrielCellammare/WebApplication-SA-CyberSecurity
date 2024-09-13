<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="application.util.entity.Proposal"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>

<!DOCTYPE html>
<html lang="it">
<head>
<style>
<%@include file="/WEB-INF/css/styleIndexLogged.css"%>
</style>


<meta charset="UTF-8">
<title>Homepage Web Application - Proposte progettuali</title>


<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>


<script>	
var scrittaAggiunta = false;
var isLogoutExecuted = false;
var isRefreshing = false;
var isProposteLoading = false;


// Chiamata AJAX separata per caricare la lista delle proposte quando la pagina si carica La funzione $(document).ready(function()) in jQuery viene utilizzata per assicurarsi che il codice JavaScript all'interno della funzione venga eseguito solo dopo che il documento HTML è stato completamente caricato e il DOM è pronto per essere manipolato.
$(document).ready(function() {
	loadProposalList();
	$('#closeBannerButton').hide()
	    // Gestire il cambiamento nell'input di file
    $('#uploadProposalFile').on('change', function () {
        if ($(this).val()) {
            $('#uploadButton').show(); // Mostra il bottone quando un file è selezionato
        } else {
            $('#uploadButton').hide(); // Nascondi il bottone quando nessun file è selezionato
        }
    });

    // Nascondere il bottone di upload all'inizio
    $('#uploadButton').hide();
    
	$('#bannerContent').hide();
	
	var inattivitaTimer; // Variabile globale

	function resetInattivitaTimer() {
	    if (inattivitaTimer) {
	        clearTimeout(inattivitaTimer); // Pulisce il timer precedente se esiste
	    }

	    inattivitaTimer = setTimeout(function() {
	        $.ajax({
	            type: 'POST',
	            url: 'LogoutServlet',
	            success: function() {
	                window.location.href = "userNotLoggedIndex.jsp";
	            }
	        });
	    }, 900000); // Timeout di 15 minuti (uguale per la sessione)
	}

    // Eventi che indicano attività dell'utente
    $(document).on('mousemove keypress click', function() {
        resetInattivitaTimer();
    });

    // Imposta inizialmente il timer di inattività quando la pagina si carica
    resetInattivitaTimer();

});

function uploadFile() {
	var formData = new FormData(document.getElementById('uploadForm'));
	$
			.ajax({
				url : 'UploadProposalServlet', // L'URL della servlet sul server a cui inviare la richiesta
				type : 'POST', // Il tipo di richiesta HTTP, in questo caso POST per inviare dati
				data : formData, //dati da inviare al server
				processData : false, // Impedisce a jQuery di elaborare automaticamente i dati (utile per l'invio di dati binari come i file)
				contentType : false, // Impedisce a jQuery di impostare automaticamente il tipo di contenuto, necessario quando si inviano dati binari
				success : function(data,textStatus,xhr) {  // Callback che viene eseguito quando la richiesta ha successo
					// Aggiorna il contenuto del banner con il risultato della risposta AJAX
					  var newCsrfToken = xhr.getResponseHeader('X-CSRF-Token');
			          if (newCsrfToken) {
			                $('#uploadForm [name="csrfToken"]').val(newCsrfToken);
			            }
					$('#bannerContent').html(data);
					$('#bannerContent').show();
					$('#uploadButton').hide();
					$('#closeBannerButton').show()
					$('#uploadProposalFile').val('');
					
				},
				error : function(xhr, status, error) {
					console.error(
							'Errore durante il caricamento del file:',
							status, error);
			        if (xhr.status === 401) {  // Se ricevi 401, reindirizza l'utente alla pagina di login
			            window.location.href = 'userNotLoggedIndex.jsp';
			        }

				}
				
			});
	
	
}


// Funzione per caricare la lista delle proposte
function loadProposalList() {
    // Controlla se la lista delle proposte è già in fase di caricamento
    if (isProposteLoading) {
        console.log("Il caricamento delle proposte è già in corso.");
        return;
    }

    isProposteLoading = true; // Imposta la variabile a true per evitare richieste duplicate

    $.ajax({
        url: 'UploadProposalServlet', // URL dell'API o del server per ottenere le proposte
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log("JSON ricevuto dal server:", data); // Log del JSON ricevuto per il debug
            
            var proposalList = $('#proposalBannerList'); // Seleziona l'elemento della lista delle proposte
            proposalList.empty(); // Svuota la lista esistente per evitare duplicati

            if (!scrittaAggiunta) {
                proposalList.append('<p><strong>Lista proposte progettuali</strong></p>'); // Aggiunge un titolo una sola volta
                scrittaAggiunta = false;
            }

            $.each(data, function (index, proposal) {
                console.log("Proposta corrente:", proposal); // Log della proposta corrente per il debug

                // Crea un nuovo link con gli attributi data-* per la proposta
                var link = $('<a>', {
                    class: 'file-link',
                    href: '#',
                    'data-email': proposal.email,
                    'data-fileName': proposal.fileName,
                    'data-html': proposal.htmlContent,
                    text: 'Email: ' + proposal.email + ', Proposta: ' + proposal.fileName
                });

                // Log di controllo per assicurarsi che gli attributi siano impostati correttamente
                console.log("link data-fileName:", link.attr('data-fileName')); // Verifica il valore di fileName
                console.log("link data-email:", link.attr('data-email')); // Verifica il valore di email
                console.log("link data-html:", link.attr('data-html')); // Verifica il valore di html

                proposalList.append($('<li>').append(link)); // Aggiungi il link alla lista delle proposte
            });

            // Gestione del clic sui link delle proposte
            $('.file-link').on('click', function (event) {
                event.preventDefault(); // Previeni il comportamento predefinito del link

                // Recupera i valori degli attributi data-* dal link cliccato
                var fileName = $(this).attr('data-fileName');
    			var email = $(this).attr('data-email');
    			var html = $(this).attr('data-html');

                // Log per verificare i valori recuperati
                console.log("fileName:", fileName);
                console.log("email:", email);
                console.log("html:", html);

                // Verifica se i valori sono mancanti e mostra un messaggio di errore se necessario
                if (fileName === undefined || html === undefined) {
                    console.error("Valori mancanti: ", fileName, html);
                    alert("Errore: valori mancanti.");
                    return;
                }

                // Crea il messaggio da mostrare nell'elemento #bannerContent
                var messaggio = "Stai visualizzando la proposta progettuale <strong>" + fileName + "</strong> di <strong>" + email + "</strong><br><br>";
                $('#bannerContent').show()
                $('#bannerContent').html('<p>' + messaggio + '</p>' + html);
                $('#closeBannerButton').show()
            });

            isProposteLoading = false; // Reimposta la variabile a false per consentire ulteriori richieste
        },
        error: function (xhr, status, error) {
            console.error("Errore durante il caricamento delle proposte:", error); // Log dell'errore
            isProposteLoading = false; // Reimposta la variabile a false anche in caso di errore
            alert("Errore nel caricamento delle proposte. Riprova più tardi."); // Mostra un messaggio di errore all'utente
        }
    });
}


var isLogoutExecuted = false;
var isRefreshing = false;


function logout() {
    if (!isLogoutExecuted) {
        // Effettua una chiamata al server per invalidare la sessione ed eliminare cookie
        $.ajax({
            url: 'LogoutServlet',
            type: 'GET',
            success: function () {
                $('#proposalBannerList').empty();
                localStorage.removeItem('email');

                // Reindirizza alla pagina di login dopo il logout
                window.location.href = "userNotLoggedIndex.jsp";
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




// Chiamata alla funzione durante il caricamento della pagina
document.addEventListener("DOMContentLoaded", function() {
    loadProposalList(); // Aggiunta la chiamata alla funzione loadProposalList
    
    // Quando la pagina è completamente caricata
    var bannerContent = $('#bannerContent');
    if (bannerContent.is(':empty')) {
        bannerContent.html('<p>Non stai visualizzando nessuna proposta</p>');
    }
});

function closeBanner() {
    $('#bannerContent').hide();  // Nascondi il contenuto del banner
    $('#closeBannerButton').hide();  // Nascondi il bottone di chiusura
}

</script>

</head>
<body>
	<div class="navbar">
		<h1>Proposte progettuali</h1>
		<div class="navbar-buttons">
			<button id="closeBannerButton" class="button" onclick="closeBanner()">Chiudi
				proposta</button>
			<a href="userNotLoggedIndex.jsp" class="button">Homepage</a>	
			<button id="refreshProposals" type="button" class="button"
				onclick="loadProposalList()">Aggiorna Lista Proposte</button>

			<button type="button" class="button" value="Logout"
				onclick="logout()">Logout</button>

		</div>
	</div>
	<h1 class="welcome-message">Benvenuto, ${email}!</h1>


	<!-- Form di caricamento proposta -->
	<form id="uploadForm" method="post" class="container"
		action="javascript:void(0);" enctype="multipart/form-data"
		onsubmit="uploadFile()">
		<input type="hidden" name="userEmail" value="${email}" />
		<div id="bannerContent"></div>
		<input type="hidden" name="csrfToken"
			value="${sessionScope.csrfToken}" />
		<div id="proposalBannerList"></div>
		<div id="form-container">
			<table>
				<tr>
					<td class="loading-proposal"><b>Carica proposta progettuale</b></td>
					<td><input id="uploadProposalFile" type="file" name="proposal"
						autocomplete="off"></td>
				</tr>
				<tr>
					<td></td>
					<td>Il file deve essere di formato .txt e pesare massimo 10
						MB.</td>
				</tr>
				<tr>
					<td></td>
					<td><input id="uploadButton" type="submit" class="button"
						value="Carica Proposta"></td>
				</tr>
			</table>
		</div>
	</form>

</body>
</html>
