package application.util.filter;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ThreadSafe
public final class UserLoggedFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		// Inizializzazione se necessaria
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		

		final String LOGIN_PAGE = "userNotLoggedLogin.jsp";

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Imposta le intestazioni di cache per impedire il caching delle pagine protette
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		// Recupera la sessione corrente, se esiste
		HttpSession session = httpRequest.getSession(false);
		boolean isLoggedInProtected = (session != null && session.getAttribute("email") != null);

		// Stampa di debug per la sessione e l'email
		if (session != null) {
			System.out.println("isLOGGEDINProtected: " + isLoggedInProtected);
			System.out.println("EmailProtected: " + session.getAttribute("email"));
		}

		// Se l'utente non � loggato, reindirizza alla pagina di login
		if (!isLoggedInProtected) {
			httpResponse.sendRedirect(LOGIN_PAGE);
			return;
		} 

		chain.doFilter(request, response);

	}


	@Override
	public void destroy() {
		// Pulizia se necessaria
	}
}
