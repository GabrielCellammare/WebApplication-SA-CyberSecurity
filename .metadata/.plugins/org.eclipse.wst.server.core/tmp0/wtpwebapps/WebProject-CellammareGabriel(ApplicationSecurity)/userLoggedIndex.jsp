<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="application.util.entity.Proposal"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>

<!DOCTYPE html>
<html lang="it">
<head>
<style><%@include file="/WEB-INF/css/styleIndexLogged.css"%>
</style>
<script>
    
var isUserLoggedIn = <%=request.getAttribute("login")%>;

if (!isUserLoggedIn || isUserLoggedIn == null) {
    // L'utente non è autenticato, reindirizzalo alla pagina di login
    window.location.href = "userNotLoggedLogin.jsp";
} 
</script>

<meta charset="UTF-8">
<title>Homepage Web Application - Proposte progettuali</title>


<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

<script>
	
var scrittaAggiunta = false;
var isLogoutExecuted = false;
var isRefreshing = false;
var isProposteLoading = false;


// Chiamata AJAX separata per caricare la lista delle proposte quando la pagina si carica
$(document).ready(function() {
	loadProposalList();
});

//Funzione per gestire la comparsa del bottone
$(document).ready(function () {
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
		
		var content = "<%=request.getAttribute("cleanedHtml")%>"
		console.log("CONTENUTO",content)
		
		if (content != "null") {
			$('#bannerContent').show();
		}

});



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
						$('#uploadButton').hide();
					},
					error : function(xhr, status, error) {
						console.error(
								'Errore durante il caricamento del file:',
								status, error);
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
                var messaggio = "Stai visualizzando la proposta progettuale <strong>" + fileName + "</strong> di <strong>" + email + "</strong>";
                $('#bannerContent').show()
                $('#bannerContent').html('<p>' + messaggio + '</p>' + html);
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
        // Effettua una chiamata al server per invalidare la sessione
        $.ajax({
            url: 'LogoutServlet',
            type: 'GET',
            success: function () {
                $('#proposalBannerList').empty();
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

/*$(document).ready(function () {
    // Gestore del clic per il bottone di aggiornamento
    $('#refreshProposals').on('click', function () {
        loadProposalList(); // Chiama la funzione per aggiornare la lista delle proposte
    });
});
*/
function checkAuthentication() {
	var userEmail = "<%=request.getAttribute("email") != null ? request.getAttribute("email") : ""%>"; // Ottieni il valore dalla JSP
	console.log("Email:", userEmail); // Usa il nome corretto della variabile

    if (!userEmail || userEmail.trim() === "") {
        window.location.href = "userNotLoggedLogin.jsp";
    }
}

// Chiamata alla funzione durante il caricamento della pagina
document.addEventListener("DOMContentLoaded", function() {
    checkAuthentication();
    loadProposalList(); // Aggiunta la chiamata alla funzione loadProposalList
    
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
		<div id="bannerContent"><%=request.getAttribute("cleanedHtml")%></div>
		<div id="proposalBannerList"></div>
		<div class="form-container">
			<table>
				<tr>
					<td>Carica proposta progettuale</td>
					<td><input id="uploadProposalFile" type="file" name="proposal"
						autocomplete="off"></td>

				</tr>
				<tr>
					<td></td>
					<td><small>Il file deve essere di formato .txt e
							pesare massimo 20 MB.</small></td>
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