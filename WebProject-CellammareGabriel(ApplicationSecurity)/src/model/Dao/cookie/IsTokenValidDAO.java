package model.Dao.cookie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.annotation.concurrent.Immutable;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;
@Immutable
public final class IsTokenValidDAO {
	
	/**
	 * Metodo che controlla la validit� del token
	 * @param cookieByte
	 * @return
	 */
	public static boolean isTokenValid(byte[] cookieByte){
		boolean status = false;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_read = DatabaseConnection.getConnectionRead();PreparedStatement ps_cookieToken = con_read.prepareStatement(DatabaseQuery.isTokenValid())){

				ps_cookieToken.setString(1, Base64.getEncoder().encodeToString(cookieByte));

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
