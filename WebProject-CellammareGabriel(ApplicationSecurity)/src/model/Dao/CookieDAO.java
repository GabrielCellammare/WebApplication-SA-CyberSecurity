package model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class CookieDAO {
	public static boolean isTokenValidDAO(String token){
		boolean status = false;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_read = DatabaseConnection.getConnectionRead();PreparedStatement ps_cookieToken = con_read.prepareStatement(DatabaseQuery.isTokenValid())){

				ps_cookieToken.setString(1, token);

				try (ResultSet rs = ps_cookieToken.executeQuery()) {
					// Controlla se esiste una riga nel ResultSet
					if (rs.next()) {
						status = true;
						DisplayMessage.showPanel("Cookie ancora valido!");
					} else {
						DisplayMessage.showPanel("Cookie non pi� valido!");
					}
				}

				//PasswordManager.clearBytes(token);
				return status;



			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}

		return status;

	}

	public static boolean deleteToken(String token){
		boolean status = false;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_delete = DatabaseConnection.getConnectionDelete();PreparedStatement ps_deleteCookie = con_delete.prepareStatement(DatabaseQuery.deleteCookie())){

				ps_deleteCookie.setString(1, token);

				int rowsAffected = ps_deleteCookie.executeUpdate();

				// Imposta lo stato a true se almeno una riga � stata aggiornata
				status = (rowsAffected > 0);

				if (status) {
					DisplayMessage.showPanel("Cookie eliminato con successo");
					return status;

				} 


				else{
					DisplayMessage.showPanel("Cookie ancora valido");

					return false;
				}

			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}

		return status;

	}


	public static String getEmailFromTokenDAO(String token){
		int id_user=0;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_read = DatabaseConnection.getConnectionRead();PreparedStatement ps_cookieID = con_read.prepareStatement(DatabaseQuery.getEmailFromToken())){

				ps_cookieID.setString(1, token);

				try(ResultSet rsId = ps_cookieID.executeQuery()){ 
					if (rsId.next()) {
						id_user=rsId.getInt("id_user");
						System.out.println("\nID = " + rsId.getInt("id_user"));
						try(PreparedStatement ps_cookieEmail=con_read.prepareStatement(DatabaseQuery.takeUserEmail())){
							ps_cookieEmail.setInt(1,id_user);
							try(ResultSet rsEmail=ps_cookieEmail.executeQuery()){
								if(rsEmail.next()) {
									return rsEmail.getString("email");
								}else {
									DisplayMessage.showPanel("Cookie1: Errore nell'individuazione dell'email nel DB. Riprovare!");
									return null;
								}
							}
						}

					}else {
						DisplayMessage.showPanel("Cookie2: Errore nell'individuazione dell'ID nel DB. Riprovare!");
						return null;
					}
				}


			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}

		return null;

	}


	//Cookie orfani
	public static void cleanupExpiredTokens(){

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			// Pseudo-codice per la pulizia dei token scaduti
			try (Connection con_delete = DatabaseConnection.getConnectionDelete();PreparedStatement ps_cookieRoutineDelete = con_delete.prepareStatement(DatabaseQuery.deleteCookieRoutine())) {

				int rowsDeleted = ps_cookieRoutineDelete.executeUpdate();
				if(rowsDeleted>0) {
					System.out.println("Pulizia dei token scaduti: " + rowsDeleted + " token eliminati.");
				}else {
					System.out.println("Nessun token scaduto");
				}
			} 

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}


	}

	public static boolean tokenExists(String token){
		boolean status = false;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_read = DatabaseConnection.getConnectionRead();PreparedStatement ps_cookieToken = con_read.prepareStatement(DatabaseQuery.tokenExists())){

				ps_cookieToken.setString(1, token);

				try (ResultSet rs = ps_cookieToken.executeQuery()) {
					// Controlla se esiste una riga nel ResultSet
					if (rs.next()) {
						status = true;
						DisplayMessage.showPanel("Cookie esistente!");

					} else {
						DisplayMessage.showPanel("Cookie precedentemente eliminato!");
					}
				}

				//PasswordManager.clearBytes(token);
				return status;



			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}

		return status;

	}


}
