package model.Dao.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.concurrent.Immutable;
@Immutable
public final class DatabaseQuery {

    private static Properties appProperties;

    static {
        appProperties = new Properties();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
            if (input == null) {
                System.err.println("File di configurazione config.ini non trovato.");
            } else {
                appProperties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento del file di configurazione.");
            e.printStackTrace();
        }
    }


    public static String getSelectUserQuery() {
        return getQuery("db.query_userLogin");
    }

    public static String registrationUserQuery() {
        return getQuery("db.query_userRegistration");
    }

    public static String registrationUserSaltQuery() {
        return getQuery("db.query_userSaltRegistration");
    }

    public static String takeUserId() {
        return getQuery("db.query_takeUserId");
    }

    public static String takeUserEmail() {
        return getQuery("db.query_takeUserEmail");
    }

    public static String userAlreadyExist() {
        return getQuery("db.query_userAlreadyExist");
    }

    public static String takeUserSalt() {
        return getQuery("db.query_takeUserSalt");
    }

    public static String insertCookie() {
        return getQuery("db.query_insertCookie");
    }

    public static String insertProposal() {
        return getQuery("db.query_insertProposal");
    }

    public static String takeEmailAndProposal() {
        return getQuery("db.query_takeEmailAndProposal");
    }

    public static String isTokenValid() {
        return getQuery("db.query_isTokenValid");
    }

    public static String deleteCookie() {
        return getQuery("db.query_deleteCookie");
    }

    public static String getEmailFromToken() {
        return getQuery("db.query_getEmailFromToken");
    }

    public static String deleteCookieRoutine() {
        return getQuery("db.query_deleteCookieRoutine");
    }

    public static String tokenExists() {
        return getQuery("db.query_tokenExists");
    }
    
    private static String getQuery(String key) {
        return appProperties.getProperty(key);
    }

}
