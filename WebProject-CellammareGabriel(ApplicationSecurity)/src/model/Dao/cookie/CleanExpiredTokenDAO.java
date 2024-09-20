package model.Dao.cookie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.concurrent.Immutable;

import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

@Immutable
public final class CleanExpiredTokenDAO {
	
	/*
	 * Metodo che si occupa di eliminare i cookie scaduti richiamato dalla routine giornaliera
	 */
	public static void cleanupExpiredToken(){

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			// codice per la pulizia dei token scaduti
			try (Connection con_delete = DatabaseConnection.getConnectionDelete();PreparedStatement ps_cookieRoutineDelete = con_delete.prepareStatement(DatabaseQuery.deleteCookieRoutine())) {

				int rowsDeleted = ps_cookieRoutineDelete.executeUpdate();
				if(rowsDeleted>0) {
					System.out.println("Pulizia dei token scaduti: " + rowsDeleted + " token eliminati.");
				}else {
					System.out.println("Nessun token scaduto");
				}
			} 

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}


	}

}
