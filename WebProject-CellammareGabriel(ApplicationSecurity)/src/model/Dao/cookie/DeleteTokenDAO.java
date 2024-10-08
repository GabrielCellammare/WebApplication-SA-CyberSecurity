package model.Dao.cookie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import javax.annotation.concurrent.Immutable;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;
@Immutable
public final class DeleteTokenDAO {

	/**
	 * Metodo che elimina il cookie dal Database se scaduto
	 * @param cookieByte
	 * @return
	 */
	public static boolean deleteToken(byte[] cookieByte){
		boolean status = false;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_delete = DatabaseConnection.getConnectionDelete();PreparedStatement ps_deleteCookie = con_delete.prepareStatement(DatabaseQuery.deleteCookie())){

				ps_deleteCookie.setString(1, Base64.getEncoder().encodeToString(cookieByte));

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

}
