package model.Dao.cookie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class IsTokenValidDAO {
	
	public static boolean isTokenValid(String token){
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
						DisplayMessage.showPanel("Cookie scaduto non pi� valido!");
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