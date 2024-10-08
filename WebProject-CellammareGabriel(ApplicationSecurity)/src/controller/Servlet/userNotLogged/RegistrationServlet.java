package controller.Servlet.userNotLogged;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import application.util.fileChecker.EmailChecker;
import application.util.fileChecker.ImageProfileFileChecker;
import model.Dao.registration.RegistrationDAO;



@MultipartConfig
@ThreadSafe
@WebServlet("/RegistrationServlet")
/**
 * Servlet che si occupa della registrazione dell'utente verificando che i parametri siano corretti e che non esista un altro utente
 * registrato con la stessa email
 * @author gabri
 *
 */
public final class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email = request.getParameter("email");
		byte[] password = request.getParameter("password").getBytes();
		byte[] retypePassword = request.getParameter("retype-password").getBytes();
		Part filePart = request.getPart("profileImage");

		if (EmailChecker.isValidEmail(email) && email.length()<=45) {
			if (PasswordManager.isStrongPassword(password)) {
				if (Arrays.equals(password, retypePassword)) {
					
					//Inizio del TOC (Time of check)
					
					
					byte[] checksum = Encryption.calculateChecksumFromPart(filePart);
					
					if (ImageProfileFileChecker.checkImageFile(filePart, checksum)) {

						byte[] salt = PasswordManager.generateRandomBytes(16);
						byte[] SaltedPassword = PasswordManager.concatenateAndHash(password, salt);
						
						PasswordManager.clearBytes(password);
						PasswordManager.clearBytes(retypePassword);

						try {
							if (RegistrationDAO.userRegistration(email, SaltedPassword, salt, filePart,checksum)) {

								email = null;
								filePart = null;
								PasswordManager.clearBytes(SaltedPassword);
								PasswordManager.clearBytes(salt);
								DisplayMessage.showPanel("Registrazione effettuata correttamente!");
								response.sendRedirect("userNotLoggedIndex.jsp");
							} else {
								email = null;
								filePart = null;
								PasswordManager.clearBytes(SaltedPassword);
								PasswordManager.clearBytes(salt);
								DisplayMessage.showPanel("Errore durante la registrazione nel DB!");
								request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);
								

							}
						} catch (Exception e) {
							e.printStackTrace();
							email = null;
							filePart = null;
							PasswordManager.clearBytes(SaltedPassword);
							PasswordManager.clearBytes(salt);
							DisplayMessage.showPanel("Errore durante la registrazione a causa di problemi di connessione!");
							request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);
						}


					} else {
						email = null;
						filePart = null;
						PasswordManager.clearBytes(password);
						PasswordManager.clearBytes(retypePassword);
						DisplayMessage.showPanel("Immagine non valida!");
						request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);
						
					}
				} else {
					email = null;
					filePart = null;
					PasswordManager.clearBytes(password);
					PasswordManager.clearBytes(retypePassword);
					DisplayMessage.showPanel("Le password non corrispondono!");
					request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);
				

				}
			} else {
				email = null;
				filePart = null;
				PasswordManager.clearBytes(password);
				PasswordManager.clearBytes(retypePassword);
				DisplayMessage.showPanel("La password non rispetta i requisiti minimi di sicurezza!");
				request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);
			}

		} else {
			email = null;
			filePart = null;
			PasswordManager.clearBytes(password);
			PasswordManager.clearBytes(retypePassword);
			request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);
			DisplayMessage.showPanel("L'email contiene caratteri non validi o eccede la lunghezza massima!");
		}
	}

}




