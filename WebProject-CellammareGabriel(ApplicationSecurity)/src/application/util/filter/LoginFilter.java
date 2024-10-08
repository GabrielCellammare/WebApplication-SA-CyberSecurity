package application.util.filter;

import java.io.IOException;
import java.util.Base64;

import javax.annotation.concurrent.Immutable;
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

import application.util.ConvertingType;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import application.util.entity.UserLogged;
import model.Dao.cookie.DeleteTokenDAO;
import model.Dao.cookie.GetEmailFromTokenDAO;
import model.Dao.cookie.IsTokenValidDAO;
import model.Dao.cookie.TokenExistsDAO;

@Immutable
/**
 * Filtro che intercetta tutte le richiesta alla pagina userNotLoggedLogin.jsp
 * @author gabri
 *
 */
public final class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	/**
	 * Verifica che la sessione o i cookie siano presenti impostando un flag, di conseguenza effettua gli indirizzamenti opportuni
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final String LOGIN_PAGE = "userNotLoggedLogin.jsp";
		final String INDEX_PAGE = "userLoggedIndex.jsp";

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Imposta le intestazioni di cache per impedire il caching delle pagine protette
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		// Recupera la sessione corrente, se esiste
		HttpSession session = httpRequest.getSession(false);
		boolean isLoggedIn = (session != null && session.getAttribute("email") != null);
		//System.out.println("Sessione: "+ httpRequest.getSession(false).toString());
		
		// Stampa di debug per la sessione e l'email
		if (session != null) {
			System.out.println("isLOGGEDIN: " + isLoggedIn);
			System.out.println("Email: " + session.getAttribute("email"));
		}

		// Determina se la richiesta corrente � per la pagina di login
		String requestURI = httpRequest.getRequestURI();
		boolean isLoginRequest = requestURI.endsWith(LOGIN_PAGE);


		// Gestione autenticazione tramite cookie se presenti
		if (!isLoggedIn) {
			Cookie[] cookies = httpRequest.getCookies();
			if (cookies != null) {
				boolean cookieRemembermeFound=false;
				for (Cookie cookie : cookies) {
					if ("rememberMe".equals(cookie.getName())) {
						cookieRemembermeFound=true;
						byte[] cookieByte = Base64.getDecoder().decode(cookie.getValue());
						System.out.println("Token cookie individuato: " + cookie.getValue());

						//Verifico prima che il cookie esista e che non sia stato eliminato dalla routine
						if(TokenExistsDAO.tokenExists(cookieByte)) {


							//Verifico che sia valido (data di scadenza)
							if (IsTokenValidDAO.isTokenValid(cookieByte)) {
								// Autenticazione tramite cookie riuscita
								String email = GetEmailFromTokenDAO.getEmailFromToken(cookieByte);

								byte[] byte_email = ConvertingType.stringToByteArray(email);
								byte[] pad_email = Encryption.addPadding(byte_email);
								byte[] byte_encryptedEmail = null;

								try {
									byte_encryptedEmail = Encryption.encrypt(pad_email);
								} catch (Exception e) {
									e.printStackTrace();
									DisplayMessage.showPanel("Errore interno, riprovare!");
									return;
								}

								//Utente senza cookie token poich� gi� presente e registrato
								UserLogged userlogged = new UserLogged(byte_encryptedEmail,cookieByte);

								session = httpRequest.getSession(true);  // Crea una nuova sessione
								session.setAttribute("email", email);
								session.setAttribute("login", true);
								session.setAttribute("csrfToken", Base64.getEncoder().encodeToString(userlogged.getCsrfToken()));

								System.out.println("Token CSRF individuato la seconda volta: " + Base64.getEncoder().encodeToString(userlogged.getCsrfToken()));
								session.setAttribute("userLogged", userlogged);
								session.setMaxInactiveInterval(15*60);
								PasswordManager.clearBytes(cookieByte);
								isLoggedIn = true;  // Utente autenticato
								break;
							} else {
								// Token non valido, rimuovi il cookie
								if (DeleteTokenDAO.deleteToken(cookieByte)) {
									Cookie expiredCookie = new Cookie("rememberMe", null);
									expiredCookie.setMaxAge(0);
									expiredCookie.setHttpOnly(true);
									expiredCookie.setSecure(true);
									httpResponse.addCookie(expiredCookie);
								}
								
								PasswordManager.clearBytes(cookieByte);
								isLoggedIn = false;  // Imposta come non autenticato
								break;
							}
						}else {
							
							//Cookie non trovato
							Cookie expiredCookie = new Cookie("rememberMe", null);
							expiredCookie.setMaxAge(0);
							expiredCookie.setHttpOnly(true);
							expiredCookie.setSecure(true);
							httpResponse.addCookie(expiredCookie);
							
							PasswordManager.clearBytes(cookieByte);
							isLoggedIn = false;  // Imposta come non autenticato
							break;

						}
						
					}
					
				}
				
				if(!cookieRemembermeFound) {
					DisplayMessage.showPanel("Nessun cookie di tipo 'rememberme' individuato!");
					
				}
			}
			else {
				DisplayMessage.showPanel("Nessun cookie esistente");
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
