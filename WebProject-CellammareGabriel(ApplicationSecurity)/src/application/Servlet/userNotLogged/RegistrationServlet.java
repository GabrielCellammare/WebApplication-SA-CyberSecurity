package application.Servlet.userNotLogged;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import application.util.EmailChecker;
import application.util.FileChecker;
import application.util.PasswordManager;

/**
 * Servlet implementation class RegistrationServlet
 */

@WebServlet(description = "Servlet for user Registration", urlPatterns = { "/RegistrationServlet" })
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
					if (FileChecker.checkImageFile(filePart)) {
						System.out.println("File valido");

						byte[] salt = PasswordManager.generateRandomBytes(16);
						byte[] SaltedPassword = PasswordManager.concatenateAndHash(password, salt);

						try {
							if (RegistrazioneDao.userRegistration(email, SaltedPassword, salt, filePart)) {

								email = null;
								PasswordManager.clearBytes(SaltedPassword);
								PasswordManager.clearBytes(password);
								PasswordManager.clearBytes(retypePassword);
								PasswordManager.clearBytes(salt);

								response.sendRedirect("login.jsp");
							} else {

								email = null;
								PasswordManager.clearBytes(SaltedPassword);
								PasswordManager.clearBytes(password);
								PasswordManager.clearBytes(retypePassword);
								PasswordManager.clearBytes(salt);
								CustomMessage.showPanel("Errore durante la registrazione!");
								request.getRequestDispatcher("registration.jsp").forward(request, response);
								
							}
						} catch (Exception e) {
							e.printStackTrace();
							CustomMessage.showPanel("Errore durante la registrazione!");
							request.getRequestDispatcher("registration.jsp").forward(request, response);
						}
					} else {
						CustomMessage.showPanel("Immagine non valida!");
						request.getRequestDispatcher("registration.jsp").forward(request, response);

					}
				} else {
					CustomMessage.showPanel("Le password non corrispondono!");
					request.getRequestDispatcher("registration.jsp").forward(request, response);

				}
			} else {
				CustomMessage.showPanel("La password non rispetta i requisiti minimi di sicurezza!");
				request.getRequestDispatcher("registration.jsp").forward(request, response);

			}

		} else {

			CustomMessage.showPanel("Il nome contiene caratteri non validi!");
			request.getRequestDispatcher("registration.jsp").forward(request, response);

		}
	}

}