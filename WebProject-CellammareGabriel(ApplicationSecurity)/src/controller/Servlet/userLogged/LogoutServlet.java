package controller.Servlet.userLogged;

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

import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.cookie.DeleteTokenDAO;


/**
 * Servlet implementation class LogoutServlet
 */
@ThreadSafe
@WebServlet("/LogoutServlet")
/**
 * Servlet che si occupa del logout dell'utente
 * @author gabri
 *
 */
public final class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogoutServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Richiesta get per effettuare il logout manuale eliminando cookie e invalidando la sessione
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			DisplayMessage.showPanel("Sessione invalidata correttamente!");
		}
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("rememberMe".equals(cookie.getName())) {
					byte[] cookieByte = Base64.getDecoder().decode(cookie.getValue()); 
					if(DeleteTokenDAO.deleteToken(cookieByte)) {
						// Rimuove il cookie dal browser
						cookie.setMaxAge(0);
						cookie.setHttpOnly(true);
						cookie.setSecure(true);
						response.addCookie(cookie);
						
						DisplayMessage.showPanel("Logout manuale effettuato correttamente!");
						
					}
					
					PasswordManager.clearBytes(cookieByte);
					
				}else {
					DisplayMessage.showPanel("Logout manuale senza cookie effettuato correttamente!");
				}
			}
		}
		
		
	}
	
	/**
	 * Richiesta post invalidando la sessione
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//Invalido solo la sessione, non distruggo i cookie
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			DisplayMessage.showPanel("Sessione invalidata correttamente!");
		}
	
		DisplayMessage.showPanel("Logout automatico effettuato correttamente!");
	}
}



