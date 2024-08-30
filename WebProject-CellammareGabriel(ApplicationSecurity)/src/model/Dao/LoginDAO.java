package model.Dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class LoginDAO {
	public static boolean isUserValid(String email, byte[] password) {

		boolean status = false;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try(Connection con_read = DatabaseConnection.getConnectionRead()){
				int id_user=TakeUserIdDAO.takeUserId(con_read, email);		

				if(id_user>0) {

					Blob userSaltBlob = TakeUserSaltDAO.takeUserSalt(con_read, id_user);

					if (userSaltBlob != null) {
						byte[] sale = userSaltBlob.getBytes(1, (int) userSaltBlob.length());
						byte[] newPassword = PasswordManager.concatenateAndHash(password, sale);

						try(PreparedStatement psUser = con_read.prepareStatement(DatabaseQuery.getSelectUserQuery())){
							psUser.setString(1, email);
							psUser.setBytes(2, newPassword);

							try(ResultSet rsUser = psUser.executeQuery()){
								status = rsUser.next();

								email = null;
								PasswordManager.clearBytes(password);
								PasswordManager.clearBytes(sale);
								PasswordManager.clearBytes(newPassword);

								if (status) {
									System.out.println("Utente trovato");
								} else {
									DisplayMessage.showPanel("Errore nell'inserimento dei dati dell'utente. Riprova"); //VALUTARE DI ELIMINARE?
								}
							} 
						}

					} else {
						email = null;
						PasswordManager.clearBytes(password);
						System.out.println("Salt � nullo");
					}
				} else {
					email = null;
					PasswordManager.clearBytes(password);
					DisplayMessage.showPanel("ID Non valido. Riprovare con un mail corretta!"); //VALUTARE DI ELIMINARE?
					System.out.println("Nessun risultato trovato per l'utente: " + email);
				}
			} 


		}catch(ClassNotFoundException|SQLException e) {
			DisplayMessage.showPanel("Si � verificato un problema di connessione!");
			e.printStackTrace();
		}
		return status;
	}
	public static boolean storeCookie(byte[] byte_encryptedEmail, String token){

		boolean status = false;
		String email=null;


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
				email=Encryption.byteArrayToString(decryptedEmailBytes);

				int id_user=TakeUserIdDAO.takeUserId(con_read, email);

				try(Connection con_write=DatabaseConnection.getConnectionWrite();
						PreparedStatement ps_cookie = con_write.prepareStatement(DatabaseQuery.insertCookie())){


					ps_cookie.setInt(1, id_user);
					ps_cookie.setString(2,token);

					int rowsAffected = ps_cookie.executeUpdate();

					// Imposta lo stato a true se almeno una riga � stata aggiornata
					status = (rowsAffected > 0);

					if (status) {

						email = null;
						PasswordManager.clearBytes(decryptedEmailBytes);
						DisplayMessage.showPanel("Cookie registrato correttamente!");
						return status;

					} 


					else{
						email = null;
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