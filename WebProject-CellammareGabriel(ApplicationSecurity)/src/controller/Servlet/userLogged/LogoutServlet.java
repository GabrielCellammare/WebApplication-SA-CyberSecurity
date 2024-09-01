package controller.Servlet.userLogged;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import application.util.customMessage.DisplayMessage;
import model.Dao.cookie.DeleteTokenDAO;


/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
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
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("rememberMe".equals(cookie.getName())) {
					String cookie_TokenString = cookie.getValue();
					DeleteTokenDAO.deleteToken(cookie_TokenString);
					// Rimuove il cookie dal browser
					cookie.setMaxAge(0);
					cookie.setHttpOnly(true);
					cookie.setSecure(true);
					response.addCookie(cookie);
				}
			}
		}
		
		DisplayMessage.showPanel("Logout manuale effettuato correttamente!");
		response.sendRedirect("userNotLoggedLogin.jsp");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		
		DisplayMessage.showPanel("Logout automatico effettuato correttamente!");
	}
}



