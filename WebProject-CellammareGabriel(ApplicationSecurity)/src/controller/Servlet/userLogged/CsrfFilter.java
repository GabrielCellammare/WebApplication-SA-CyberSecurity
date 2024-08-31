package controller.Servlet.userLogged;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CsrfFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro, se necessaria
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

    	System.out.println("Dentro il filtro");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
		// Imposta le intestazioni di cache per impedire il caching delle pagine protette
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", 0);


        String method = httpRequest.getMethod();
        System.out.println("Metodo: " + method);
        System.out.println("Metodo: " + method);
        System.out.println("csrfToken Richiesta: " + httpRequest.getParameter("csrfToken"));
        System.out.println("csrfToken Richiesta: " + (String) session.getAttribute("csrfToken"));
        
        // Controllo solo le richieste POST, PUT e DELETE per CSRF
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
        	System.out.println("Dentro il filtro POST");
            String csrfToken = httpRequest.getParameter("csrfToken");
            String sessionToken = (session != null) ? (String) session.getAttribute("csrfToken") : null;
            
            if (csrfToken == null || sessionToken == null || !csrfToken.equals(sessionToken)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Richiesta non valida: token CSRF mancante o non valido.");
                return; // Blocca la richiesta
            }
        } 

        // Procedi con la catena di filtri
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Pulizia del filtro, se necessaria
    }


}
