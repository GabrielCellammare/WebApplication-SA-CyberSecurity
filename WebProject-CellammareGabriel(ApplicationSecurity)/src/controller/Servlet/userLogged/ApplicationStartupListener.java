package controller.Servlet.userLogged;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.Dao.CookieDAO;

@WebListener // Annotazione che registra il listener
public class ApplicationStartupListener implements ServletContextListener {

    // Esecutore per il task di pulizia
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Creazione dell'esecutore con un singolo thread
        scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // Pianifica la pulizia dei token scaduti ogni 24 ore
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Logica di pulizia dei token
                CookieDAO.cleanupExpiredTokens();
            }
        }, 0, 24, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Interrompe l'esecutore quando il contesto viene distrutto
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
