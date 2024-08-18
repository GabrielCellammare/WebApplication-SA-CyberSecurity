package controller.Servlet.userNotLogged;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import application.util.EmailChecker;
import application.util.ImageProfileFileChecker;
import application.util.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.RegistrationDAO;



@MultipartConfig
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
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

		if (EmailChecker.isValidEmail(email)) {
			if (PasswordManager.isStrongPassword(password)) {
				if (Arrays.equals(password, retypePassword)) {
					if (ImageProfileFileChecker.checkImageFile(filePart)) {

						byte[] salt = PasswordManager.generateRandomBytes(16);
						byte[] SaltedPassword = PasswordManager.concatenateAndHash(password, salt);
						PasswordManager.clearBytes(password);
						PasswordManager.clearBytes(retypePassword);

						try {
							if (RegistrationDAO.userRegistration(email, SaltedPassword, salt, filePart)) {

								email = null;
								filePart = null;
								PasswordManager.clearBytes(SaltedPassword);
								PasswordManager.clearBytes(salt);
								response.sendRedirect("userNotLoggedLogin.jsp");
								DisplayMessage.showPanel("Registrazione effettuata correttamente!");

							} else {

								email = null;
								PasswordManager.clearBytes(SaltedPassword);
								PasswordManager.clearBytes(salt);

								DisplayMessage.showPanel("Errore durante la registrazione!");
								request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);

							}
						} catch (Exception e) {
							e.printStackTrace();
							DisplayMessage.showPanel("Errore durante la registrazione!");
							PasswordManager.clearBytes(SaltedPassword);
							PasswordManager.clearBytes(password);
							PasswordManager.clearBytes(retypePassword);
							PasswordManager.clearBytes(salt);
							request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);
						}


					} else {
						email=null;
						filePart=null;
						PasswordManager.clearBytes(password);
						PasswordManager.clearBytes(retypePassword);
						DisplayMessage.showPanel("Immagine non valida!");
						request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);

					}
				} else {
					email=null;
					filePart=null;
					PasswordManager.clearBytes(password);
					PasswordManager.clearBytes(retypePassword);
					DisplayMessage.showPanel("Le password non corrispondono!");
					request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);

				}
			} else {
				email=null;
				filePart=null;
				PasswordManager.clearBytes(password);
				PasswordManager.clearBytes(retypePassword);
				DisplayMessage.showPanel("La password non rispetta i requisiti minimi di sicurezza!");
				request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);

			}

		} else {
			email=null;
			filePart=null;
			PasswordManager.clearBytes(password);
			PasswordManager.clearBytes(retypePassword);
			DisplayMessage.showPanel("L'email contiene caratteri non validi!");
			request.getRequestDispatcher("userNotLoggedRegistration.jsp").forward(request, response);

		}
	}

}




