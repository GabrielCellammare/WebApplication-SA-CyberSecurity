package controller.Servlet.userLogged;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import application.util.ProposalChecker;
import application.util.customMessage.DisplayMessage;
import application.util.entity.Proposal;
import model.Dao.ProposalDAO;

/**
 * Servlet implementation class UploadProposalServlet
 */
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Part filePart = request.getPart("Proposta progettuale");
		String nomeUtente = request.getParameter("nomeUtente");

		if (ProposalChecker.checkProposalFile(filePart, getServletContext())) {

			String cleanedHtml = ProposalChecker.processFile(filePart);

			String nomeFile = ProposalChecker.getFileName(filePart);
			// Aggiungi il contenuto filtrato all'oggetto request
			request.setAttribute("contenutoFiltrato", cleanedHtml);
			byte[] htmlBytes = cleanedHtml.getBytes(StandardCharsets.UTF_8);

			try {
				if (ProposalDAO.uploadFile(nomeUtente,nomeFile,htmlBytes)) {
					DisplayMessage.showPanel("La proposta � stata correttamente caricata!");

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
			DisplayMessage.showPanel("File non valido!");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
