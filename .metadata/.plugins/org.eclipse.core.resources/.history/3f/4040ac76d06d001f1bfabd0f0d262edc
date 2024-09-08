package model.Dao.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.concurrent.Immutable;

import application.util.customMessage.DisplayMessage;
@Immutable
public final class DatabaseConnection {

	private final static String URL;
	private final static String USERNAME_READ;
	private final static String PASSWORD_READ;
	
	private final static String USERNAME_WRITE;
	private final static String PASSWORD_WRITE;
	
	private final static String USERNAME_DELETE;
	private final static String PASSWORD_DELETE;
	
	static {
		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			Properties prop = new Properties();
			prop.load(input);

			URL = prop.getProperty("db.url");
			USERNAME_READ = prop.getProperty("db.username_read");
			PASSWORD_READ= prop.getProperty("db.password_read");

			USERNAME_WRITE = prop.getProperty("db.username_write");
			PASSWORD_WRITE = prop.getProperty("db.password_write");
			
			USERNAME_DELETE = prop.getProperty("db.username_delete");
			PASSWORD_DELETE = prop.getProperty("db.password_delete");
			
			// Log per il debug
			System.out.println("Configurazione caricata correttamente:");
			System.out.println("URL: " + URL);
			System.out.println("USERNAME_DELETE: " + USERNAME_DELETE);
			// NOTA: NON stampare la password in ambienti di produzione o log pubblici!
			System.out.println("PASSWORD_DELETE: " + PASSWORD_DELETE); 
			
		} catch (IOException e) {
			e.printStackTrace();
			DisplayMessage.showPanel("Sembra esserci un problema con qualche file di configurazione!");
			throw new RuntimeException("Errore nel caricamento del file di configurazione", e);
		}
	}

	public static Connection getConnectionRead() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME_READ, PASSWORD_READ);
	}

	public static Connection getConnectionWrite() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME_WRITE, PASSWORD_WRITE);
	}
	
	public static Connection getConnectionDelete() throws SQLException {
		// Log per il debug prima di tentare la connessione
		System.out.println("Tentativo di connessione al database con utente DELETE...");
		System.out.println("URL: " + URL);
		System.out.println("USERNAME_DELETE: " + USERNAME_DELETE);
		
		try {
			return DriverManager.getConnection(URL, USERNAME_DELETE, PASSWORD_DELETE);
		} catch (SQLException e) {
			// Log per il debug in caso di errore
			System.err.println("Errore nella connessione DELETE: " + e.getMessage());
			throw e;  // Rilancia l'eccezione dopo il log
		}
	}
}
