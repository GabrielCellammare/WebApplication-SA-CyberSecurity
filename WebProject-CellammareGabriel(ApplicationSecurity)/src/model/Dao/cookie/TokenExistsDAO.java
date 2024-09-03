package model.Dao.cookie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class TokenExistsDAO {

	public static boolean tokenExists(byte[] cookieByte){
		boolean status = false;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_read = DatabaseConnection.getConnectionRead();PreparedStatement ps_cookieToken = con_read.prepareStatement(DatabaseQuery.tokenExists())){

				ps_cookieToken.setString(1, Base64.getEncoder().encodeToString(cookieByte));

				try (ResultSet rs = ps_cookieToken.executeQuery()) {
					// Controlla se esiste una riga nel ResultSet
					if (rs.next()) {
						status = true;
						DisplayMessage.showPanel("Cookie esistente!");

					} else {
						DisplayMessage.showPanel("Cookie precedentemente eliminato!");
					}
				}

				return status;

			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}

		return status;

	}
}
