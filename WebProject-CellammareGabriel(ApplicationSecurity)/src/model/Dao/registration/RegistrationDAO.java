package model.Dao.registration;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import javax.annotation.concurrent.Immutable;
import javax.servlet.http.Part;

import application.util.cryptography.Encryption;
import application.util.customMessage.DisplayMessage;
import model.Dao.TakeUserIdDAO;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;
@Immutable
public final class RegistrationDAO {

	/**
	 * Registrazione nel Database dell'utente verificando che non ci sia un altro utente registrato con la stessa email e che l'immagine profilo 
	 * non sia stata sostituita (TOC-TOU)
	 * @param email
	 * @param password
	 * @param salt
	 * @param filePart
	 * @param checksumOriginal
	 * @return
	 * @throws IOException
	 */
	public static boolean userRegistration(String email, byte[] password, byte[] salt, Part filePart, byte[] checksumOriginal)
			throws IOException {

		try {
			//Inizializza il driver per comunicare con il dB
			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection con_write = DatabaseConnection.getConnectionWrite();
					Connection con_read = DatabaseConnection.getConnectionRead()){

				// Verifica se l'utente esiste gi�
				if (UserAlreadyExistDAO.userAlreadyExists(con_read, email ) > 0) {
					DisplayMessage.showPanel("Utente gi� registrato! Email gi� presente");
					return false; 
				}


				//registratione nella tabella user
				try (PreparedStatement ps = con_write.prepareStatement(DatabaseQuery.registrationUserQuery())) {
					ps.setString(1, email);
					ps.setBytes(2, password);

					try(InputStream fileContent = filePart.getInputStream()){
						
						byte[] lastChecksum = Encryption.calculateChecksumFromPart(filePart);


						if(!Arrays.equals(checksumOriginal, lastChecksum)) {
							DisplayMessage.showPanel("Non � stato possibile terminare la registrazione, le immagini profilo non risultano uguali!");
							return false; 
						}


						ps.setBlob(3, fileContent);


						int rowsAffected = ps.executeUpdate();
						
						// Imposta lo stato a true se almeno una riga � stata aggiornata
						boolean status = (rowsAffected > 0);

						//se effettuata correttamente, effettuo la query per ricavare l'id dell'utente registrato
						if (status) {
							int id_user=TakeUserIdDAO.takeUserId(con_read, email);

							//una volta individuato l'id, registro tramite l'id il salt nella tabella apposita

							if(id_user>0) {
								return (RegistrationUserSaltDAO.registrationUserSalt(con_write, id_user, salt) > 0); // Restituisci true se anche la seconda query ha avuto successo
							}
							else {
								DisplayMessage.showPanel("ID Non valido. Riprovare con un mail corretta. Sale non registrato!"); 

							}

						}else {
							DisplayMessage.showPanel("Non � stato possibile terminare la registrazione. Riprovare!"); 

						}
					}

				}
			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 

			DisplayMessage.showPanel("Non � stato possibile terminare la registrazione!");
		}
		return false; 
	}


}
