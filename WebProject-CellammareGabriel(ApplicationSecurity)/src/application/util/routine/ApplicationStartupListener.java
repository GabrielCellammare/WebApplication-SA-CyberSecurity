
package application.util.routine;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Dao.cookie.CleanExpiredTokenDAO;

@ThreadSafe
@WebListener // Annotazione che registra il listener
/**
 * Listener che effettua la pulizia dei cookie orfani ogni 24 ore (Cookie scaduti)
 * @author gabri
 *
 */
public final class ApplicationStartupListener implements ServletContextListener {

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
                CleanExpiredTokenDAO.cleanupExpiredToken();
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
