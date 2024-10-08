package model.Dao.cookie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import javax.annotation.concurrent.Immutable;

import application.util.ConvertingType;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.TakeUserIdDAO;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;
@Immutable
public final class StoreCookieDAO {

	/**
	 * Metodo che si occupa dell'archiviazione dei cookie all'interno del Database
	 * @param byte_encryptedEmail
	 * @param token
	 * @return
	 */
	public static boolean storeCookie(byte[] byte_encryptedEmail, byte[] token){

		boolean status = false;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_read = DatabaseConnection.getConnectionRead()){



				byte[] decryptedEmailBytes=null;
				try {
					decryptedEmailBytes = Encryption.decrypt(byte_encryptedEmail);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				decryptedEmailBytes=Encryption.removePadding(decryptedEmailBytes);
				int id_user=TakeUserIdDAO.takeUserId(con_read, ConvertingType.byteArrayToString(decryptedEmailBytes));

				try(Connection con_write=DatabaseConnection.getConnectionWrite();
						PreparedStatement ps_cookie = con_write.prepareStatement(DatabaseQuery.insertCookie())){


					ps_cookie.setInt(1, id_user);
					ps_cookie.setString(2,Base64.getEncoder().encodeToString(token));

					int rowsAffected = ps_cookie.executeUpdate();

					// Imposta lo stato a true se almeno una riga � stata aggiornata
					status = (rowsAffected > 0);

					if (status) {

						PasswordManager.clearBytes(decryptedEmailBytes);
						DisplayMessage.showPanel("Cookie registrato correttamente!");
						return status;

					} 


					else{
						PasswordManager.clearBytes(decryptedEmailBytes);
						DisplayMessage.showPanel("Cookie non registrato correttamente!");

						return false;
					}

				}
			}
		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}

		return status;

	}


}
