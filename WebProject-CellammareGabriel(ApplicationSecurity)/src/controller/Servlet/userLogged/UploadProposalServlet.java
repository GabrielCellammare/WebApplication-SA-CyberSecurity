package controller.Servlet.userLogged;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import application.util.ProposalChecker;
import application.util.cryptography.Encryption;
import application.util.customMessage.DisplayMessage;
import application.util.entity.Proposal;
import application.util.entity.UserLogged;
import model.Dao.proposal.ProposalDAO;

/**
 * Servlet implementation class UploadProposalServlet
 */
@MultipartConfig
@WebServlet("/UploadProposalServlet")
public class UploadProposalServlet extends HttpServlet {
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
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		List<Proposal> proposte = Proposal.getProposals();
		
		// Converti la lista in JSON
		String jsonProposte = new Gson().toJson(proposte);

		// Invia la risposta JSON
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonProposte);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Part filePart = request.getPart("proposal");
		String email = request.getParameter("userEmail");
		boolean boolSecureCsfr=false;

		System.out.println(email);
		System.out.println("Dentro il filtro POST 1");

        HttpSession session = request.getSession(false);
       

        String method = request.getMethod();
        System.out.println("Metodo: " + method);
        System.out.println("csrfToken Richiesta: " + request.getParameter("csrfToken"));
        System.out.println("csrfToken Richiesta: " + (String) session.getAttribute("csrfToken"));
    	UserLogged userlogged = (UserLogged) session.getAttribute("userLogged");
    	
        // Controllo solo le richieste POST, PUT e DELETE per CSRF
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
        	System.out.println("Dentro il filtro POST 2");
            String csrfToken = request.getParameter("csrfToken");
            String sessionCsrfToken = (session != null) ? (String) session.getAttribute("csrfToken") : null;
            
            if (csrfToken == null || sessionCsrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            	boolSecureCsfr=false;
            }else {
            	boolSecureCsfr=true;
            }
        } 
        
		if (ProposalChecker.checkProposalFile(filePart, getServletContext()) && boolSecureCsfr) {
			
			userlogged.setCsrfTokenString();
			String newCsrfToken=userlogged.getCsrfTokenString();
			session.setAttribute("csrfToken", newCsrfToken);

			System.out.println("New CSFRF TOKEN " + newCsrfToken);
    		
			String cleanedHtml = ProposalChecker.processFile(filePart);

			String filename = ProposalChecker.getFileName(filePart);
			byte[] htmlBytes = cleanedHtml.getBytes(StandardCharsets.UTF_8);

			try {
				if (ProposalDAO.uploadFile(email,filename,htmlBytes)) {
					DisplayMessage.showPanel("La proposta � stata correttamente caricata!");
					response.setHeader("X-CSRF-Token", newCsrfToken);
					// Invia il contenuto filtrato come risposta AJAX
					response.setContentType("text/plain");
					response.getWriter().write(cleanedHtml);
				} else {
					DisplayMessage.showPanel("Non � stato possibile caricare il file della proposta!");
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			DisplayMessage.showPanel("File non valido o sessione non autentica!");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
