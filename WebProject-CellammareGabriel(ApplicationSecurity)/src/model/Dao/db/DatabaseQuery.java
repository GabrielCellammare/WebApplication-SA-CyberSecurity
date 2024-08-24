package model.Dao.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseQuery {

	public static String getSelectUserQuery() {
		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_userLogin");
	}

	public static String registrationUserQuery() {
		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
			
		} catch (IOException e) {
			System.out.println("Query DatabaseQuery - Registration");
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_userRegistration");
	}

	public static String registrationUserSaltQuery() {
		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_userSaltRegistration");

	}
	
	public static String registrationUserId() {
		
		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_registrationUserId");

	}

	public static String userAlreadyExist() {

		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			System.out.println("Query DatabaseQuery - User already Exist");
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_userAlreadyExist");
	}

	public static String takeUserSalt() {

		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			System.out.println("Query DatabaseQuery - takeUserSalt");
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_takeUserSalt");
	}
	
	public static String insertCookie() {
		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_insertCookie");

	}

	public static String insertProposal() {

		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			System.out.println("Query DatabaseQuery - insertProposta");
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_insertProposal");
	}

	public static String takeEmailAndProposal() {

		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			System.out.println("Query DatabaseQuery - Query DatabaseQuery - takeEmailAndProposal");
			e.printStackTrace();
			return null;
		}

		return appProperties.getProperty("db.query_takeEmailAndProposal");
	}

}