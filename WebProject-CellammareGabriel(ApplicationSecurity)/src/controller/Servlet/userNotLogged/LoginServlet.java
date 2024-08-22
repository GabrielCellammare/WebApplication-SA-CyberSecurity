package controller.Servlet.userNotLogged;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.LoginDAO;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");


		byte[] byte_encryptedEmail = null;
		byte[] pad_email = null;

		String email = request.getParameter("email");
		byte[] password = request.getParameter("password").getBytes();
		boolean ricordami = request.getParameter("remember") != null;
		byte[] byte_email = email.getBytes(java.nio.charset.StandardCharsets.UTF_8); 
		pad_email = Encryption.addPadding(byte_email);


		try {
			byte_encryptedEmail = Encryption.encrypt(pad_email);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String base64EncodedEmail = Base64.getEncoder().encodeToString(byte_encryptedEmail);


		if (ricordami) {

			try {
				if (LoginDAO.isUserValid(email, password)) {


					HttpSession session = request.getSession(true);
					session.setAttribute("userEmail", base64EncodedEmail);
					request.setAttribute("login", true); //Se questa variabile non viene inizializzata su true, l'utente non riesce ad accedere a benvenuto.jsp
					byte[] token = PasswordManager.generateRandomBytes(base64EncodedEmail.length());
					if(LoginDAO.storeCookie(base64EncodedEmail, token)) {

						Cookie rememberMeCookie = new Cookie("rememberme", base64EncodedEmail + ":" + token);
						rememberMeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 giorni
						rememberMeCookie.setHttpOnly(true); // Protezione contro XSS
						rememberMeCookie.setPath("/"); // Accessibile da tutto il sito
						response.addCookie(rememberMeCookie);

						PasswordManager.clearBytes(password);
						email = null;
						request.getRequestDispatcher("userLoggedIndex.jsp").forward(request, response);
					}else {
						DisplayMessage.showPanel("Sembra che la registrazione del cookie non sia andata a buon fine.");
						request.getRequestDispatcher("/userNotLoggedLogin.jsp").forward(request, response);
					}
					
				} else {
					email = null;
					PasswordManager.clearBytes(password);

					DisplayMessage.showPanel("Sembra che questo utente non esista! Controlla i dati inseriti.");
					request.getRequestDispatcher("/userNotLoggedLogin.jsp").forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			try {
				if (LoginDAO.isUserValid(email, password)) {

					request.setAttribute("login", true); //Se questa variabile non viene inizializzata su true, l'utente non riesce ad accedere a benvenuto.jsp

					PasswordManager.clearBytes(password);
					email = null;

					request.getRequestDispatcher("userLoggedIndex.jsp").forward(request, response);
				} else {

					email = null;
					PasswordManager.clearBytes(password);

					DisplayMessage.showPanel("Password errata! Riprova.");
					request.getRequestDispatcher("/userNotLoggedLogin.jsp").forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//checkCookie(request, response);
	}




/**
	private void checkCookie(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		JsonObject cookieData = new JsonObject();

		String string_encryptedUsernameCookie = null;
		String string_encryptedPasswordCookie = null;

		String usernameCookie = null;
		String passwordCookie = null;

		byte[] byte_encryptedPasswordCookie = null;
		byte[] byte_encryptedUsernameCookie = null;

		Cookie[] cookies = request.getCookies();

		boolean foundUsernameCookie = false;
		boolean foundPasswordCookie = false;

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("username")) {
					foundUsernameCookie = true;
					string_encryptedUsernameCookie = cookie.getValue();
					byte_encryptedUsernameCookie = Base64.getDecoder().decode(string_encryptedUsernameCookie);
				} else if (cookie.getName().equals("password")) {
					foundPasswordCookie = true;
					string_encryptedPasswordCookie = cookie.getValue();
					byte_encryptedPasswordCookie = Base64.getDecoder().decode(string_encryptedPasswordCookie);
				} else if (!cookie.getName().equals("JSESSIONID")) {
					// Se il cookie non si chiama "username" o "password" o "JSESSIONID", invalidalo
					cookie.setValue("");  // Imposta il valore del cookie a una stringa vuota
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}

		if (!foundUsernameCookie || !foundPasswordCookie) {
			System.out.println("Uno dei due cookie non � stato trovato, interrompo");
		} else {
			try {
				byte[] decryptedUsernameBytesCookie = Encryption.decrypt(byte_encryptedUsernameCookie);
				byte[] decryptedPasswordBytesCookie = Encryption.decrypt(byte_encryptedPasswordCookie);

				decryptedUsernameBytesCookie = Encryption.removePadding(decryptedUsernameBytesCookie);
				decryptedPasswordBytesCookie = Encryption.removePadding(decryptedPasswordBytesCookie);

				usernameCookie = Encryption.byteArrayToString(decryptedUsernameBytesCookie);
				passwordCookie = Encryption.byteArrayToString(decryptedPasswordBytesCookie);

				// RIEMPIRE IL FORM DEL login.jsp con questi dati se non sono nulli
				request.setAttribute("decryptedUsername", usernameCookie);
				request.setAttribute("decryptedPassword", passwordCookie);

				cookieData.addProperty("cookiesPresent", true);
				cookieData.addProperty("decryptedUsername", usernameCookie);
				cookieData.addProperty("decryptedPassword", passwordCookie);
			} catch (Exception e) {
				System.out.println("ERRORE CON L'ACCESSO CON I COOKIE NEL DO POST");
				e.printStackTrace();
			}
		}

		response.getWriter().write(cookieData.toString());
	}
	**/
}



