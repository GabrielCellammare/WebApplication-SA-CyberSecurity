package controller.Servlet.userLogged;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import application.util.cryptography.Encryption;
import application.util.customMessage.DisplayMessage;
import application.util.entity.UserLogged;
import model.Dao.CookieDAO;

public class LoginFilter implements Filter {

	private static final String LOGIN_PAGE = "userNotLoggedLogin.jsp";
	private static final String INDEX_PAGE = "userLoggedIndex.jsp";

	@Override
	public void init(FilterConfig config) throws ServletException {
		// Inizializzazione se necessaria
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Imposta le intestazioni di cache per impedire il caching delle pagine protette
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		// Recupera la sessione corrente, se esiste
		HttpSession session = httpRequest.getSession(false);
		boolean isLoggedIn = (session != null && session.getAttribute("email") != null);

		// Stampa di debug per la sessione e l'email
		if (session != null) {
			System.out.println("isLOGGEDIN: " + isLoggedIn);
			System.out.println("Email: " + session.getAttribute("email"));
		}

		// Determina se la richiesta corrente � per la pagina di login
		String requestURI = httpRequest.getRequestURI();
		boolean isLoginRequest = requestURI.endsWith(LOGIN_PAGE);
		boolean isIndexPageRequest = requestURI.endsWith(INDEX_PAGE);

		// Gestione autenticazione tramite cookie
		if (!isLoggedIn) {
			Cookie[] cookies = httpRequest.getCookies();
			boolean rememberMeCookieFound = false;

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("rememberMe".equals(cookie.getName())) {
						rememberMeCookieFound = true;
						String cookie_TokenString = cookie.getValue();
						System.out.println("Token individuato la seconda volta: " + cookie_TokenString);

						if(CookieDAO.tokenExists(cookie_TokenString)) {


							if (CookieDAO.isTokenValidDAO(cookie_TokenString)) {
								// Autenticazione tramite cookie riuscita
								String email = CookieDAO.getEmailFromTokenDAO(cookie_TokenString);

								byte[] byte_email = email.getBytes(java.nio.charset.StandardCharsets.UTF_8); 
								byte[] pad_email = Encryption.addPadding(byte_email);
								byte[] byte_encryptedEmail = null;

								try {
									byte_encryptedEmail = Encryption.encrypt(pad_email);
								} catch (Exception e) {
									e.printStackTrace();
									DisplayMessage.showPanel("Errore interno, riprovare!");
									return;
								}

								UserLogged userlogged = new UserLogged(byte_encryptedEmail,cookie_TokenString);

								session = httpRequest.getSession(true);  // Crea una nuova sessione
								session.setAttribute("email", email);
								session.setAttribute("login", true);
								session.setAttribute("csrfToken", userlogged.getCsrfTokenString());
								System.out.println("Token individuato la seconda volta: " + userlogged.getCsrfTokenString());
								session.setAttribute("userLogged", userlogged);
								session.setMaxInactiveInterval(15 * 60);
								isLoggedIn = true;  // Utente autenticato
								break;
							} else {
								// Token non valido, rimuovi il cookie
								if (CookieDAO.deleteToken(cookie_TokenString)) {
									Cookie expiredCookie = new Cookie("rememberMe", null);
									expiredCookie.setMaxAge(0);
									expiredCookie.setHttpOnly(true);
									expiredCookie.setSecure(true);
									httpResponse.addCookie(expiredCookie);
								}
								isLoggedIn = false;  // Imposta come non autenticato
								break;
							}
						}else {
							Cookie expiredCookie = new Cookie("rememberMe", null);
							expiredCookie.setMaxAge(0);
							expiredCookie.setHttpOnly(true);
							expiredCookie.setSecure(true);
							httpResponse.addCookie(expiredCookie);

							isLoggedIn = false;  // Imposta come non autenticato
							break;

						}
					}
				}
			}
		}

		// Logica di reindirizzamento basata sull'autenticazione
		if (isLoggedIn) {
			// Se l'utente � autenticato e tenta di accedere alla pagina di login, reindirizzalo alla pagina principale
			if (isLoginRequest) {
				httpResponse.sendRedirect(INDEX_PAGE);
				return;  // Esci dalla funzione dopo il reindirizzamento
			} else {
				// Utente autenticato e non sta tentando di accedere alla pagina di login, continua con la richiesta
				chain.doFilter(request, response);
				return;  // Esci dalla funzione dopo aver continuato con la richiesta
			}
		} else {
			// Utente non autenticato
			if (isLoginRequest) {
				// Se l'utente non � autenticato ma sta accedendo alla pagina di login, permetti la richiesta
				chain.doFilter(request, response);
			} else {
				// Utente non autenticato e sta tentando di accedere a una pagina protetta, reindirizzalo alla pagina di login
				httpResponse.sendRedirect(LOGIN_PAGE);
			}
			return;  // Esci dalla funzione dopo aver gestito la richiesta
		}
	}

	@Override
	public void destroy() {
		// Pulizia se necessaria
	}
}
