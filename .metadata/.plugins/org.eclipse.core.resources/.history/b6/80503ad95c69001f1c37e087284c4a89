package model.Dao.registration;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import javax.servlet.http.Part;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.TakeUserIdDAO;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class RegistrationDAO {


	public static boolean userRegistration(String email, byte[] password, byte[] salt, Part filePart, byte[] checksumOriginal)
			throws IOException {

		boolean check = false;
		try {
			//Inizializza il driver per comunicare con il dB
			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_write = DatabaseConnection.getConnectionWrite();
					Connection con_read = DatabaseConnection.getConnectionRead()){

				// Verifica se l'utente esiste già
				if (UserAlreadyExistDAO.userAlreadyExists(con_read, email ) > 0) {
					DisplayMessage.showPanel("Utente già registrato! Email già presente");
					return false; 
				}


				//registratione nella tabella user
				try (PreparedStatement ps = con_write.prepareStatement(DatabaseQuery.registrationUserQuery())) {
					ps.setString(1, email);
					ps.setBytes(2, password);

					try(InputStream fileContent = filePart.getInputStream()){

						byte[] lastChecksum = PasswordManager.calculateChecksum(filePart);

						check=Arrays.equals(checksumOriginal, lastChecksum);

						if(!check) {
							DisplayMessage.showPanel("Non è stato possibile terminare la registrazione, i file non risultano uguali!");
							return false; 
						}


						ps.setBlob(3, fileContent);


						int rowsAffected = ps.executeUpdate();
						
						// Imposta lo stato a true se almeno una riga è stata aggiornata
						boolean status = (rowsAffected > 0);

						//se effettuata correttamente, effettuo la query per ricavare l'id dell'utente registrato
						if (status) {
							int id_user=TakeUserIdDAO.takeUserId(con_read, email);

							//una volta individuato l'id, registro tramite l'id il salt nella tabella apposita

							if(id_user>0) {
								return (RegistrationUserSaltDAO.registrationUserSalt(con_write, id_user, salt) > 0); // Restituisci true se anche la seconda query ha avuto successo
							}
							else {
								DisplayMessage.showPanel("ID Non valido. Riprovare con un mail corretta. Sale non registrato!"); //VALUTARE DI ELIMINARE?

							}

						}else {
							DisplayMessage.showPanel("Non è stato possibile terminare la registrazione. Riprovare!"); //VALUTARE DI ELIMINARE?

						}
					}

				}
			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 

			DisplayMessage.showPanel("Non è stato possibile terminare la registrazione!");
		}
		return false; 
	}


}
