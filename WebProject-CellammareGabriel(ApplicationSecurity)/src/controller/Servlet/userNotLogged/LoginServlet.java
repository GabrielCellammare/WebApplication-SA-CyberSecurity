package controller.Servlet.userNotLogged;

import java.io.IOException;
import java.util.Base64;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import application.util.ConvertingType;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import application.util.entity.UserLogged;
import application.util.fileChecker.EmailChecker;
import model.Dao.cookie.StoreCookieDAO;
import model.Dao.login.LoginDAO;


/**
 * Servlet che gestisce il login
 */
@ThreadSafe
@WebServlet("/LoginServlet")
public final class LoginServlet extends HttpServlet {
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
	 * Verifica che email e password siano corrette e istanzia se necessario i cookie 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final int COOKIE_MAX_AGE = 60 * 60 * 24; // 1 giorno

		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		String email = request.getParameter("email");
		
		if(!EmailChecker.isValidEmail(email)) {
			email = null;
			response.sendRedirect("userNotLoggedIndex.jsp");  // Reindirizzamento in caso di email non valida, sanificazione dell'input
			DisplayMessage.showPanel("Caratteri email non validi!");
			return;
		}
		
		byte[] byte_email = ConvertingType.stringToByteArray(email);
		byte[] pad_email = Encryption.addPadding(byte_email);
		byte[] byte_encryptedEmail = null;

		try {
			byte_encryptedEmail = Encryption.encrypt(pad_email);
		} catch (Exception e) {
			e.printStackTrace();
			DisplayMessage.showPanel("Errore interno, riprovare!");
			response.sendRedirect("userNotLoggedLogin.jsp");  // Reindirizzamento in caso di errore critico
			return;
		}

		byte[] password = request.getParameter("password").getBytes();

		boolean ricordami = request.getParameter("remember") != null;

		if (LoginDAO.isUserValid(email, password)) {

			UserLogged userlogged = new UserLogged(byte_encryptedEmail);

			HttpSession session = request.getSession(); //Crea una nuova sessione
			session.setAttribute("email", email);
			session.setAttribute("login", true);
			session.setAttribute("csrfToken", Base64.getEncoder().encodeToString(userlogged.getCsrfToken()));
			session.setAttribute("userLogged", userlogged);
			session.setMaxInactiveInterval(15*60); // 15 minuti di timeout della sessione

			if (ricordami) {

				byte[] cookie = userlogged.getCookieToken();

				if (StoreCookieDAO.storeCookie(byte_encryptedEmail, cookie)) {
					Cookie rememberMeCookie = new Cookie("rememberMe", Base64.getEncoder().encodeToString(cookie));
					rememberMeCookie.setMaxAge(COOKIE_MAX_AGE);
					rememberMeCookie.setHttpOnly(true);
					rememberMeCookie.setSecure(true);
					response.addCookie(rememberMeCookie);


					// Redirect to logged-in user page
					response.sendRedirect("userLoggedIndex.jsp");
					PasswordManager.clearBytes(cookie);
				} else {
					response.sendRedirect("userNotLoggedLogin.jsp");  // Reindirizzamento in caso di problema con i cookie
					PasswordManager.clearBytes(cookie);
					DisplayMessage.showPanel("C'� stato un problema con i cookie. Riprova!");
				}
			} else {

				PasswordManager.clearBytes(password);
				PasswordManager.clearBytes(byte_email);
				PasswordManager.clearBytes(pad_email);
				PasswordManager.clearBytes(byte_encryptedEmail);
				email = null;
				// Se "ricordami" non � selezionato, continua senza impostare il cookie
				response.sendRedirect("userLoggedIndex.jsp");
				DisplayMessage.showPanel("Login senza meccanismo dei cookie effettuato correttamente!");
			}
		} else {

			PasswordManager.clearBytes(password);
			PasswordManager.clearBytes(byte_email);
			PasswordManager.clearBytes(pad_email);
			PasswordManager.clearBytes(byte_encryptedEmail);
			email = null;
			response.sendRedirect("userNotLoggedLogin.jsp");  // Reindirizzamento in caso di autenticazione fallita
			DisplayMessage.showPanel("Password errata! Riprovare");
		}


	}



}



