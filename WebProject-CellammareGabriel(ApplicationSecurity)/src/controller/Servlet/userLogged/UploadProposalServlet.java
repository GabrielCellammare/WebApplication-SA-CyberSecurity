package controller.Servlet.userLogged;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import application.util.ConvertingType;
import application.util.ReadByteSecure;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import application.util.entity.Proposal;
import application.util.entity.UserLogged;
import application.util.fileChecker.ProposalChecker;
import model.Dao.proposal.ProposalDAO;

/**
 * Servlet implementation class UploadProposalServlet
 */
@MultipartConfig
@ThreadSafe
@WebServlet("/UploadProposalServlet")
/**
 * Servlet che si occupa della gestione del caricamento e della visualizzazione delle proposte
 * @author gabri
 *
 */
public final class UploadProposalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadProposalServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Restituisce in formato json tutte quante le proposte presenti in una lista restituita dal metodo statico della classe Proposal
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		List<Proposal> proposte = Proposal.getProposals();

		// Converti la lista in JSON
		String jsonProposte = new Gson().toJson(proposte);

		// Invia la risposta JSON
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonProposte);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Carica le proposte nel Database prima di aver verificato la sessione e il Token CSRF
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Part filePart = request.getPart("proposal");
		byte[] fileContent=null;

		String email = request.getParameter("userEmail");

		boolean boolSecureCsfr=false;

		System.out.println("Email individuata durante il caricamento della propsota: " + email);
		System.out.println("Dentro il filtro POST 1");

		HttpSession session = request.getSession(false);

		if(session==null) {
			DisplayMessage.showPanel("Sessione scaduta!");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Status 401: Unauthorized
			return;
		}

		String method = request.getMethod();
		System.out.println("Metodo: " + method);
		System.out.println("csrfToken Richiesta: " + request.getParameter("csrfToken"));
		System.out.println("csrfToken Sessione: " + (String) session.getAttribute("csrfToken"));
		UserLogged userlogged = (UserLogged) session.getAttribute("userLogged");

		// Controllo solo le richieste POST, PUT e DELETE per CSRF
		if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
			System.out.println("Dentro il filtro POST 2");
			char[] csrfToken = ConvertingType.parseStringtoCharArray(request.getParameter("csrfToken")); //Prende il token dal file di richiesta jsp userLoggedIndex
			char[] sessionCsrfToken = (session != null) ? ConvertingType.parseStringtoCharArray((String)session.getAttribute("csrfToken")) : null;

			if (csrfToken == null || sessionCsrfToken == null || !ConvertingType.areCharArraysEqual(sessionCsrfToken,csrfToken)) {
				boolSecureCsfr=false;
			}else {
				boolSecureCsfr=true;
			}


			System.out.println("Lunghezza token CSRF richiesta" + csrfToken.length);
			System.out.println("Lunghezza token CSRF sessione" + sessionCsrfToken.length);
			System.out.println("I token sono uguali: " + ConvertingType.areCharArraysEqual(sessionCsrfToken,csrfToken));
			java.util.Arrays.fill(csrfToken, '\0');
			java.util.Arrays.fill(sessionCsrfToken, '\0');

		} 
		//Inizio toc
		byte[] checksumOriginalFile = Encryption.calculateChecksumFromPart(filePart);

		try (InputStream inputStream = filePart.getInputStream()) {
			fileContent = ReadByteSecure.readAllBytesSecurely(inputStream);
		}catch(IOException e) {
			e.printStackTrace();
		}

		byte[] checksumOriginalContent = Encryption.calculateChecksumFile(fileContent);


		ProposalChecker.checksumControl(request, response, session, checksumOriginalFile, checksumOriginalContent);
		//Se il checksum � diverso, invalida la sessione e i cookie

		PasswordManager.clearBytes(checksumOriginalFile);


		if (ProposalChecker.checkProposalFile(request, response, session, filePart, getServletContext(), checksumOriginalContent) && boolSecureCsfr) {

			String cleanedHtml = ProposalChecker.processFile(request, response, session, filePart,checksumOriginalContent);

			String filename = ProposalChecker.getFileName(filePart);
			byte[] htmlBytes = ConvertingType.stringToByteArray(cleanedHtml);

			try {
				if (ProposalDAO.uploadFile(request, response, session, email,filename,htmlBytes,checksumOriginalContent)) {

					userlogged.setCsrfToken();
					byte[] newCsrfToken=userlogged.getCsrfToken();
					session.setAttribute("csrfToken", Base64.getEncoder().encodeToString(newCsrfToken));

					System.out.println("New CSFRF TOKEN " + Base64.getEncoder().encodeToString(newCsrfToken));

					response.setHeader("X-CSRF-Token", Base64.getEncoder().encodeToString(newCsrfToken));
					// Invia il contenuto filtrato come risposta AJAX
					response.setContentType("text/plain");

					response.getWriter().write(cleanedHtml);
					PasswordManager.clearBytes(newCsrfToken);
					DisplayMessage.showPanel("La proposta � stata correttamente caricata!");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					DisplayMessage.showPanel("Non � stato possibile caricare il file della proposta!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			DisplayMessage.showPanel("File non valido o sessione non autentica!");
		}
	}



}
