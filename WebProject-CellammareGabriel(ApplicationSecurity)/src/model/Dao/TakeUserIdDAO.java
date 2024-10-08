package model.Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.concurrent.Immutable;

import application.util.customMessage.DisplayMessage;

import java.sql.Connection;
import model.Dao.db.DatabaseQuery;

@Immutable
public final class TakeUserIdDAO {

	/**
	 * Metodo che restituisce l'id univoco associato ad ogni utente attraverso la mail
	 * @param con_read
	 * @param email
	 * @return
	 */
	public static int takeUserId(Connection con_read, String email) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try(PreparedStatement psIdSelect = con_read.prepareStatement(DatabaseQuery.takeUserId())){
				psIdSelect.setString(1, email);
				try(ResultSet rsId= psIdSelect.executeQuery()){ 
					if (rsId.next()) {
						System.out.println("\nID utente = " + rsId.getInt("id"));
						return rsId.getInt("id"); 
					}else {
						DisplayMessage.showPanel("Errore nell'individuazione dell'ID nel DB. Riprovare con una mail corretta!");
						return 0;
					}
				}
			}
		}catch(ClassNotFoundException|SQLException e) {
			DisplayMessage.showPanel("Si � verificato un problema di connessione!");
			e.printStackTrace();
		}

		return 0;
	}
}



