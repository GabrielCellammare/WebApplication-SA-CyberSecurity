package model.Dao.cookie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public final class GetEmailFromTokenDAO {
	
	public static String getEmailFromToken(byte[] cookieByte){
		int id_user=0;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_read = DatabaseConnection.getConnectionRead();PreparedStatement ps_cookieID = con_read.prepareStatement(DatabaseQuery.getEmailFromToken())){

				ps_cookieID.setString(1, Base64.getEncoder().encodeToString(cookieByte));

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

}
