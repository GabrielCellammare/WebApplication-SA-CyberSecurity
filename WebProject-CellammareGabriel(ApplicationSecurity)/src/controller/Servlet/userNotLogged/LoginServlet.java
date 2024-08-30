package controller.Servlet.userNotLogged;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import application.util.entity.UserLogged;
import model.Dao.LoginDAO;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int COOKIE_MAX_AGE = 60 * 60 * 24; // 1 giorno


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

		String email = request.getParameter("email");
		byte[] password = request.getParameter("password").getBytes();
		boolean ricordami = request.getParameter("remember") != null;

		byte[] byte_email = email.getBytes(java.nio.charset.StandardCharsets.UTF_8); 
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

		if (LoginDAO.isUserValid(email, password)) {
			
			UserLogged userlogged = new UserLogged(byte_encryptedEmail);

			HttpSession session = request.getSession();
			session.setAttribute("email", email);
			session.setAttribute("login", true);
			session.setAttribute("csrfToken", userlogged.getCsrfToken());
			session.setMaxInactiveInterval(15 * 60); // 15 minuti di timeout della sessione

			if (ricordami) {
				
				String cookie_TokenString = userlogged.getCookieTokenString();
				if (LoginDAO.storeCookie(byte_encryptedEmail, cookie_TokenString)) {
					Cookie rememberMeCookie = new Cookie("rememberMe", cookie_TokenString);
					rememberMeCookie.setMaxAge(COOKIE_MAX_AGE);
					rememberMeCookie.setHttpOnly(true);
					rememberMeCookie.setSecure(true);
					response.addCookie(rememberMeCookie);
					// Redirect to logged-in user page
					response.sendRedirect("userLoggedIndex.jsp");
				} else {
					DisplayMessage.showPanel("C'� stato un problema con i cookie. Riprova!");
					response.sendRedirect("userNotLoggedLogin.jsp");  // Reindirizzamento in caso di problema con i cookie
				}
			} else {
				// Se "ricordami" non � selezionato, continua senza impostare il cookie
				DisplayMessage.showPanel("Login senza meccanismo dei cookie effettuato correttamente!");
				response.sendRedirect("userLoggedIndex.jsp");
			}
		} else {
			DisplayMessage.showPanel("Password errata! Riprovare");
			response.sendRedirect("userNotLoggedLogin.jsp");  // Reindirizzamento in caso di autenticazione fallita
		}

		// Pulizia dei dati sensibili dalla memoria
		
		//eseguire altra pulizia
		PasswordManager.clearBytes(password);
		PasswordManager.clearBytes(byte_email);
		PasswordManager.clearBytes(pad_email);
		PasswordManager.clearBytes(byte_encryptedEmail);
		email = null;
	}



}



