/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.0.36
 * Generated at: 2024-08-24 18:40:38 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.List;
import application.util.entity.Proposal;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

public final class userLoggedIndex_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/WEB-INF/css/styleIndexLogged.css", Long.valueOf(1724517436986L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("javax.servlet.http.HttpSession");
    _jspx_imports_classes.add("java.util.List");
    _jspx_imports_classes.add("javax.servlet.http.HttpServletRequest");
    _jspx_imports_classes.add("application.util.entity.Proposal");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

final java.lang.String _jspx_method = request.getMethod();
if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
return;
}

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html lang=\"it\">\r\n");
      out.write("<head>\r\n");
      out.write("<style>");
      out.write("@charset \"UTF-8\";\r\n");
      out.write("\r\n");
      out.write("body {\r\n");
      out.write("    font-family: sans-serif;\r\n");
      out.write("    background-color: #ececff;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".navbar {\r\n");
      out.write("    display: flex;\r\n");
      out.write("    align-items: center;\r\n");
      out.write("    justify-content: space-between;\r\n");
      out.write("    padding: 20px;\r\n");
      out.write("    background-color: #8ac1ff;\r\n");
      out.write("    border: 1px solid #001328;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".navbar h1 {\r\n");
      out.write("    margin: 0;\r\n");
      out.write("    font-size: 24px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".navbar-buttons {\r\n");
      out.write("    display: flex;\r\n");
      out.write("    gap: 10px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".button {\r\n");
      out.write("    background-color: #007bff;\r\n");
      out.write("    color: #fff;\r\n");
      out.write("    padding: 10px 20px;\r\n");
      out.write("    text-decoration: none;\r\n");
      out.write("    border: 1px solid black;\r\n");
      out.write("    border-radius: 10px;\r\n");
      out.write("    font-size: 20px;\r\n");
      out.write("    cursor: pointer;\r\n");
      out.write("    text-align: center;\r\n");
      out.write("    margin: 0;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".button:hover {\r\n");
      out.write("    background-color: #0056b3;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".welcome-message {\r\n");
      out.write("    text-align: center;\r\n");
      out.write("    margin-top: 10px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".container {\r\n");
      out.write("    margin: 0 auto;\r\n");
      out.write("    max-width: 80vw;\r\n");
      out.write("    padding: 5vw;\r\n");
      out.write("    background-color: #8ac1ff;\r\n");
      out.write("    opacity: 0.9;\r\n");
      out.write("    border-radius: 30px;\r\n");
      out.write("    border: 1px solid #001328;\r\n");
      out.write("    position: absolute;\r\n");
      out.write("    top: 50%;\r\n");
      out.write("    left: 50%;\r\n");
      out.write("    transform: translate(-50%, -50%);\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".form-container {\r\n");
      out.write("    margin-top: 10px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("table {\r\n");
      out.write("    width: auto;\r\n");
      out.write("    margin-top: 10px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("input[type=\"file\"] {\r\n");
      out.write("    font-size: 16px;\r\n");
      out.write("    padding: 5px;\r\n");
      out.write("    border-radius: 5px;\r\n");
      out.write("    border: 1px solid #ccc;\r\n");
      out.write("    background-color: #f9f9f9;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#contenutoBanner {\r\n");
      out.write("\tbackground-color: #f9f9f9; /* Colore sfondo del banner */\r\n");
      out.write("\tborder: 1px solid #ddd; /* Bordo del banner */\r\n");
      out.write("\tborder-radius: 5px; /* Angoli arrotondati */\r\n");
      out.write("\tpadding: 10px; /* Spaziatura interna */\r\n");
      out.write("\tmargin-top: 20px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#listaProposteBanner {\r\n");
      out.write("\tbackground-color: #fff; /* Colore sfondo del banner */\r\n");
      out.write("\tborder: 1px solid #ddd; /* Bordo del banner */\r\n");
      out.write("\tborder-radius: 5px; /* Angoli arrotondati */\r\n");
      out.write("\tpadding: 10px; /* Spaziatura interna */\r\n");
      out.write("\tmargin-top: 20px;\r\n");
      out.write("\ttext-align: center; /* Aggiunto lo stile per centrare il contenuto */\r\n");
      out.write("\tlist-style-type: none; /* Rimuovi i pallini dalla lista */\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".container {\r\n");
      out.write("    max-width: 100vw; /* Limita la larghezza del contenitore alla larghezza della finestra */\r\n");
      out.write("    overflow: hidden; /* Impedisce che il contenuto ecceda il contenitore */\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("form {\r\n");
      out.write("\tmax-width: 400px;\r\n");
      out.write("\tmargin: 50px auto;\r\n");
      out.write("\tbackground: #fff;\r\n");
      out.write("\tpadding: 20px;\r\n");
      out.write("\tborder-radius: 5px;\r\n");
      out.write("\tbox-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("table {\r\n");
      out.write("\twidth: 100%;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("table td {\r\n");
      out.write("\tpadding: 10px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("input {\r\n");
      out.write("\twidth: 100%;\r\n");
      out.write("\tpadding: 8px;\r\n");
      out.write("\tmargin-bottom: 10px;\r\n");
      out.write("\tbox-sizing: border-box;\r\n");
      out.write("\tborder: 1px solid #ccc;\r\n");
      out.write("\tborder-radius: 4px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("input[type=\"submit\"] {\r\n");
      out.write("\tbackground-color: #4caf50;\r\n");
      out.write("\tcolor: white;\r\n");
      out.write("\tcursor: pointer;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("input[type=\"submit\"]:hover {\r\n");
      out.write("\tbackground-color: #45a049;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("tr.logout-row {\r\n");
      out.write("\tdisplay: flex;\r\n");
      out.write("\tjustify-content: space-between;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("input[type=\"button\"].logout-button {\r\n");
      out.write("\tbackground-color: red;\r\n");
      out.write("\tcolor: white;\r\n");
      out.write("\tcursor: pointer;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("input[type=\"submit\"] {\r\n");
      out.write("\tbackground-color: #4caf50;\r\n");
      out.write("\tcolor: #fff;\r\n");
      out.write("\tcursor: pointer;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#contenutoBanner {\r\n");
      out.write("\tbackground-color: #f9f9f9; /* Colore sfondo del banner */\r\n");
      out.write("\tborder: 1px solid #ddd; /* Bordo del banner */\r\n");
      out.write("\tborder-radius: 5px; /* Angoli arrotondati */\r\n");
      out.write("\tpadding: 10px; /* Spaziatura interna */\r\n");
      out.write("\tmargin-top: 20px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#listaProposteBanner {\r\n");
      out.write("\tbackground-color: #fff; /* Colore sfondo del banner */\r\n");
      out.write("\tborder: 1px solid #ddd; /* Bordo del banner */\r\n");
      out.write("\tborder-radius: 5px; /* Angoli arrotondati */\r\n");
      out.write("\tpadding: 10px; /* Spaziatura interna */\r\n");
      out.write("\tmargin-top: 20px;\r\n");
      out.write("\ttext-align: center; /* Aggiunto lo stile per centrare il contenuto */\r\n");
      out.write("\tlist-style-type: none; /* Rimuovi i pallini dalla lista */\r\n");
      out.write("\r\n");
      out.write("</style>\r\n");
      out.write("<script>\r\n");
      out.write("    \r\n");
      out.write("var isUserLoggedIn = ");
      out.print(request.getAttribute("login"));
      out.write(";\r\n");
      out.write("\r\n");
      out.write("if (!isUserLoggedIn || isUserLoggedIn == null) {\r\n");
      out.write("    // L'utente non è autenticato, reindirizzalo alla pagina di login\r\n");
      out.write("    window.location.href = \"userNotLoggedLogin.jsp\";\r\n");
      out.write("} \r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<meta charset=\"UTF-8\">\r\n");
      out.write("<title>Homepage Web Application - Proposte progettuali</title>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<script src=\"https://code.jquery.com/jquery-3.6.4.min.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("\t\r\n");
      out.write("var scrittaAggiunta = false;\r\n");
      out.write("var isLogoutExecuted = false;\r\n");
      out.write("var isRefreshing = false;\r\n");
      out.write("var isProposteLoading = false;\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("// Chiamata AJAX separata per caricare la lista delle proposte quando la pagina si carica\r\n");
      out.write("$(document).ready(function() {\r\n");
      out.write("\tloadProposalList();\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("//Funzione per gestire la comparsa del bottone\r\n");
      out.write("$(document).ready(function () {\r\n");
      out.write("    // Gestire il cambiamento nell'input di file\r\n");
      out.write("    $('#uploadProposalFile').on('change', function () {\r\n");
      out.write("        if ($(this).val()) {\r\n");
      out.write("            $('#uploadButton').show(); // Mostra il bottone quando un file è selezionato\r\n");
      out.write("        } else {\r\n");
      out.write("            $('#uploadButton').hide(); // Nascondi il bottone quando nessun file è selezionato\r\n");
      out.write("        }\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("    // Nascondere il bottone di upload all'inizio\r\n");
      out.write("    $('#uploadButton').hide();\r\n");
      out.write("    \r\n");
      out.write("\t$('#bannerContent').hide();\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar content = \"");
      out.print(request.getAttribute("cleanedHtml"));
      out.write("\"\r\n");
      out.write("\t\tconsole.log(\"CONTENUTO\",content)\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tif (content != \"null\") {\r\n");
      out.write("\t\t\t$('#bannerContent').show();\r\n");
      out.write("\t\t}\r\n");
      out.write("\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function uploadFile() {\r\n");
      out.write("\t\tvar formData = new FormData(document.getElementById('uploadForm'));\r\n");
      out.write("\t\t$\r\n");
      out.write("\t\t\t\t.ajax({\r\n");
      out.write("\t\t\t\t\turl : 'UploadProposalServlet',\r\n");
      out.write("\t\t\t\t\ttype : 'POST',\r\n");
      out.write("\t\t\t\t\tdata : formData,\r\n");
      out.write("\t\t\t\t\tprocessData : false,\r\n");
      out.write("\t\t\t\t\tcontentType : false,\r\n");
      out.write("\t\t\t\t\tsuccess : function(data) {\r\n");
      out.write("\t\t\t\t\t\t// Aggiorna il contenuto del banner con il risultato della risposta AJAX\r\n");
      out.write("\t\t\t\t\t\t$('#bannerContent').html(data);\r\n");
      out.write("\t\t\t\t\t\t$('#uploadButton').hide();\r\n");
      out.write("\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\terror : function(xhr, status, error) {\r\n");
      out.write("\t\t\t\t\t\tconsole.error(\r\n");
      out.write("\t\t\t\t\t\t\t\t'Errore durante il caricamento del file:',\r\n");
      out.write("\t\t\t\t\t\t\t\tstatus, error);\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("// Funzione per caricare la lista delle proposte\r\n");
      out.write("function loadProposalList() {\r\n");
      out.write("    // Controlla se la lista delle proposte è già in fase di caricamento\r\n");
      out.write("    if (isProposteLoading) {\r\n");
      out.write("        console.log(\"Il caricamento delle proposte è già in corso.\");\r\n");
      out.write("        return;\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    isProposteLoading = true; // Imposta la variabile a true per evitare richieste duplicate\r\n");
      out.write("\r\n");
      out.write("    $.ajax({\r\n");
      out.write("        url: 'UploadProposalServlet', // URL dell'API o del server per ottenere le proposte\r\n");
      out.write("        method: 'GET',\r\n");
      out.write("        dataType: 'json',\r\n");
      out.write("        success: function (data) {\r\n");
      out.write("            console.log(\"JSON ricevuto dal server:\", data); // Log del JSON ricevuto per il debug\r\n");
      out.write("            \r\n");
      out.write("            var proposalList = $('#proposalBannerList'); // Seleziona l'elemento della lista delle proposte\r\n");
      out.write("            proposalList.empty(); // Svuota la lista esistente per evitare duplicati\r\n");
      out.write("\r\n");
      out.write("            if (!scrittaAggiunta) {\r\n");
      out.write("                proposalList.append('<p><strong>Lista proposte progettuali</strong></p>'); // Aggiunge un titolo una sola volta\r\n");
      out.write("                scrittaAggiunta = false;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            $.each(data, function (index, proposal) {\r\n");
      out.write("                console.log(\"Proposta corrente:\", proposal); // Log della proposta corrente per il debug\r\n");
      out.write("\r\n");
      out.write("                // Crea un nuovo link con gli attributi data-* per la proposta\r\n");
      out.write("                var link = $('<a>', {\r\n");
      out.write("                    class: 'file-link',\r\n");
      out.write("                    href: '#',\r\n");
      out.write("                    'data-email': proposal.email,\r\n");
      out.write("                    'data-fileName': proposal.fileName,\r\n");
      out.write("                    'data-html': proposal.htmlContent,\r\n");
      out.write("                    text: 'Email: ' + proposal.email + ', Proposta: ' + proposal.fileName\r\n");
      out.write("                });\r\n");
      out.write("\r\n");
      out.write("                // Log di controllo per assicurarsi che gli attributi siano impostati correttamente\r\n");
      out.write("                console.log(\"link data-fileName:\", link.attr('data-fileName')); // Verifica il valore di fileName\r\n");
      out.write("                console.log(\"link data-email:\", link.attr('data-email')); // Verifica il valore di email\r\n");
      out.write("                console.log(\"link data-html:\", link.attr('data-html')); // Verifica il valore di html\r\n");
      out.write("\r\n");
      out.write("                proposalList.append($('<li>').append(link)); // Aggiungi il link alla lista delle proposte\r\n");
      out.write("            });\r\n");
      out.write("\r\n");
      out.write("            // Gestione del clic sui link delle proposte\r\n");
      out.write("            $('.file-link').on('click', function (event) {\r\n");
      out.write("                event.preventDefault(); // Previeni il comportamento predefinito del link\r\n");
      out.write("\r\n");
      out.write("                // Recupera i valori degli attributi data-* dal link cliccato\r\n");
      out.write("                var fileName = $(this).attr('data-fileName');\r\n");
      out.write("    \t\t\tvar email = $(this).attr('data-email');\r\n");
      out.write("    \t\t\tvar html = $(this).attr('data-html');\r\n");
      out.write("\r\n");
      out.write("                // Log per verificare i valori recuperati\r\n");
      out.write("                console.log(\"fileName:\", fileName);\r\n");
      out.write("                console.log(\"email:\", email);\r\n");
      out.write("                console.log(\"html:\", html);\r\n");
      out.write("\r\n");
      out.write("                // Verifica se i valori sono mancanti e mostra un messaggio di errore se necessario\r\n");
      out.write("                if (fileName === undefined || html === undefined) {\r\n");
      out.write("                    console.error(\"Valori mancanti: \", fileName, html);\r\n");
      out.write("                    alert(\"Errore: valori mancanti.\");\r\n");
      out.write("                    return;\r\n");
      out.write("                }\r\n");
      out.write("\r\n");
      out.write("                // Crea il messaggio da mostrare nell'elemento #bannerContent\r\n");
      out.write("                var messaggio = \"Stai visualizzando la proposta progettuale <strong>\" + fileName + \"</strong> di <strong>\" + email + \"</strong>\";\r\n");
      out.write("                $('#bannerContent').show()\r\n");
      out.write("                $('#bannerContent').html('<p>' + messaggio + '</p>' + html);\r\n");
      out.write("            });\r\n");
      out.write("\r\n");
      out.write("            isProposteLoading = false; // Reimposta la variabile a false per consentire ulteriori richieste\r\n");
      out.write("        },\r\n");
      out.write("        error: function (xhr, status, error) {\r\n");
      out.write("            console.error(\"Errore durante il caricamento delle proposte:\", error); // Log dell'errore\r\n");
      out.write("            isProposteLoading = false; // Reimposta la variabile a false anche in caso di errore\r\n");
      out.write("            alert(\"Errore nel caricamento delle proposte. Riprova più tardi.\"); // Mostra un messaggio di errore all'utente\r\n");
      out.write("        }\r\n");
      out.write("    });\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("var isLogoutExecuted = false;\r\n");
      out.write("var isRefreshing = false;\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function logout() {\r\n");
      out.write("    if (!isLogoutExecuted) {\r\n");
      out.write("        // Effettua una chiamata al server per invalidare la sessione\r\n");
      out.write("        $.ajax({\r\n");
      out.write("            url: 'LogoutServlet',\r\n");
      out.write("            type: 'GET',\r\n");
      out.write("            success: function () {\r\n");
      out.write("                $('#proposalBannerList').empty();\r\n");
      out.write("                localStorage.removeItem('email');\r\n");
      out.write("\r\n");
      out.write("                // Reindirizza alla pagina di login dopo il logout\r\n");
      out.write("                window.location.href = \"userNotLoggedLogin.jsp\";\r\n");
      out.write("            },\r\n");
      out.write("            error: function (xhr, status, error) {\r\n");
      out.write("                console.error('Errore durante il logout:', status, error);\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("        isLogoutExecuted = true;\r\n");
      out.write("    }\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("//Gestisci il logout quando l'utente sta lasciando la pagina\r\n");
      out.write("\r\n");
      out.write("window.addEventListener('beforeunload', function (event) {\r\n");
      out.write("    isRefreshing = true;\r\n");
      out.write("\r\n");
      out.write("    console.log('beforeunload event');\r\n");
      out.write("\r\n");
      out.write("    if (!event.persisted && !isLogoutExecuted && !isRefreshing && !isProposteLoading) {\r\n");
      out.write("        // Esegui la funzione di logout solo se necessario e non è un aggiornamento della pagina\r\n");
      out.write("                console.log('Performing logout');\r\n");
      out.write("\r\n");
      out.write("        logout();\r\n");
      out.write("    }\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("// Gestisci il logout quando l'utente fa clic sul pulsante di navigazione del browser\r\n");
      out.write("window.addEventListener('popstate', function () {\r\n");
      out.write("    console.log('popstate event');\r\n");
      out.write("\r\n");
      out.write("    if (!isLogoutExecuted && !isRefreshing) {\r\n");
      out.write("        // Esegui la funzione di logout solo se necessario\r\n");
      out.write("                console.log('Performing logout');\r\n");
      out.write("\r\n");
      out.write("        logout();\r\n");
      out.write("    }\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("$(document).ready(function() {\r\n");
      out.write("      var inattivitaTimer; // Variabile per memorizzare l'ID del timer\r\n");
      out.write("\r\n");
      out.write("    // Funzione chiamata quando l'utente compie un'azione\r\n");
      out.write("    function resetInattivitaTimer() {\r\n");
      out.write("        clearTimeout(inattivitaTimer); // Resetta il timer\r\n");
      out.write("        inattivitaTimer = setTimeout(function() {\r\n");
      out.write("            // Esegui la funzione di logout dopo il periodo di inattività\r\n");
      out.write("            logout();\r\n");
      out.write("        }, 600000); // 10minuti\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    // Chiamata quando la pagina si carica\r\n");
      out.write("    $(document).on('click keypress mousemove', function() {\r\n");
      out.write("        resetInattivitaTimer();\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("/*$(document).ready(function () {\r\n");
      out.write("    // Gestore del clic per il bottone di aggiornamento\r\n");
      out.write("    $('#refreshProposals').on('click', function () {\r\n");
      out.write("        loadProposalList(); // Chiama la funzione per aggiornare la lista delle proposte\r\n");
      out.write("    });\r\n");
      out.write("});\r\n");
      out.write("*/\r\n");
      out.write("function checkAuthentication() {\r\n");
      out.write("\tvar userEmail = \"");
      out.print(request.getAttribute("email") != null ? request.getAttribute("email") : "");
      out.write("\"; // Ottieni il valore dalla JSP\r\n");
      out.write("\tconsole.log(\"Email:\", userEmail); // Usa il nome corretto della variabile\r\n");
      out.write("\r\n");
      out.write("    if (!userEmail || userEmail.trim() === \"\") {\r\n");
      out.write("        window.location.href = \"userNotLoggedLogin.jsp\";\r\n");
      out.write("    }\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("// Chiamata alla funzione durante il caricamento della pagina\r\n");
      out.write("document.addEventListener(\"DOMContentLoaded\", function() {\r\n");
      out.write("    checkAuthentication();\r\n");
      out.write("    loadProposalList(); // Aggiunta la chiamata alla funzione loadProposalList\r\n");
      out.write("    \r\n");
      out.write("    // Quando la pagina è completamente caricata\r\n");
      out.write("    var bannerContent = $('#bannerContent');\r\n");
      out.write("    if (bannerContent.is(':empty')) {\r\n");
      out.write("        bannerContent.html('<p>Non stai visualizzando nessuna proposta</p>');\r\n");
      out.write("    }\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t<div class=\"navbar\">\r\n");
      out.write("\t\t<h1>Proposte progettuali</h1>\r\n");
      out.write("\t\t<div class=\"navbar-buttons\">\r\n");
      out.write("\t\t\t<button id=\"refreshProposals\" type=\"button\" class=\"button\"\r\n");
      out.write("\t\t\t\tonclick=\"loadProposalList()\">Aggiorna Lista Proposte</button>\r\n");
      out.write("\r\n");
      out.write("\t\t\t<button type=\"button\" class=\"button\" value=\"Logout\"\r\n");
      out.write("\t\t\t\tonclick=\"logout()\">Logout</button>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<h1 class=\"welcome-message\">Benvenuto, ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${email}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("!</h1>\r\n");
      out.write("\t<!-- Form di caricamento proposta -->\r\n");
      out.write("\t<form id=\"uploadForm\" method=\"post\" class=\"container\"\r\n");
      out.write("\t\taction=\"javascript:void(0);\" enctype=\"multipart/form-data\"\r\n");
      out.write("\t\tonsubmit=\"uploadFile()\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"userEmail\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${email}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("\" />\r\n");
      out.write("\t\t<div id=\"bannerContent\">");
      out.print(request.getAttribute("cleanedHtml"));
      out.write("</div>\r\n");
      out.write("\t\t<div id=\"proposalBannerList\"></div>\r\n");
      out.write("\t\t<div class=\"form-container\">\r\n");
      out.write("\t\t\t<table>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Carica proposta progettuale</td>\r\n");
      out.write("\t\t\t\t\t<td><input id=\"uploadProposalFile\" type=\"file\" name=\"proposal\"\r\n");
      out.write("\t\t\t\t\t\tautocomplete=\"off\"></td>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td></td>\r\n");
      out.write("\t\t\t\t\t<td><small>Il file deve essere di formato .txt e\r\n");
      out.write("\t\t\t\t\t\t\tpesare massimo 20 MB.</small></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td></td>\r\n");
      out.write("\t\t\t\t\t<td><input id=\"uploadButton\" type=\"submit\" class=\"button\"\r\n");
      out.write("\t\t\t\t\t\tvalue=\"Carica Proposta\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</form>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
