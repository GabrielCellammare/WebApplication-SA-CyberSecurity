package model.Dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Part;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class RegistrationDAO {
	
	
	public static boolean userRegistration(String email, byte[] password, byte[] salt, Part filePart)
			throws IOException {
		Connection con_write = null;
		Connection con_read = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			con_write = DatabaseConnection.getConnectionWrite();
			con_read = DatabaseConnection.getConnectionRead();

			// Verifica se l'utente esiste gi�
			if (userAlreadyExists(email, con_read) > 0) {
				DisplayMessage.showPanel("Utente gi� registrato!");
				return false; 
			}

			
			try (PreparedStatement ps = con_write.prepareStatement(DatabaseQuery.registrationUserQuery())) {
				ps.setString(1, email);
				ps.setBytes(2, password);

				InputStream fileContent = filePart.getInputStream();
				ps.setBlob(3, fileContent);

				int rowsAffected = ps.executeUpdate();

				// Imposta lo stato a true se almeno una riga � stata aggiornata
				boolean status = (rowsAffected > 0);
	
				if (status) {
					try (PreparedStatement psSalt = con_write.prepareStatement(DatabaseQuery.registrationUserSaltQuery())) {
						psSalt.setString(1, email);
						psSalt.setBytes(2, salt);

						int rowsAffectedSale = psSalt.executeUpdate();

						return (rowsAffectedSale > 0); // Restituisci true se anche la seconda query ha avuto successo
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		} finally {
			// Chiusura delle connessioni
			if (con_write != null) {
				try {
					con_write.close();
				} catch (SQLException e) {
					e.printStackTrace(); 
				}
			}
			if (con_read != null) {
				try {
					con_read.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DisplayMessage.showPanel("Non � stato possibile terminare la registrazione!");
		return false; 
	}

	private static int userAlreadyExists(String username, Connection connection) throws SQLException {
		String query = DatabaseQuery.userAlreadyExist();

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("count"); 
				}
			}
		}

		return 0; 
	}
	
	

}
