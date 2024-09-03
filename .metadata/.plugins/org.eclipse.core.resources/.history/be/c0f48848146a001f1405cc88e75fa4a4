package model.Dao.login;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseQuery;

class TakeUserSaltDAO {

	static Blob takeUserSalt(Connection con_read, int id_user) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try(PreparedStatement ps_salt = con_read.prepareStatement(DatabaseQuery.takeUserSalt())){
				ps_salt.setInt(1, id_user);
				try(ResultSet rsSalt = ps_salt.executeQuery()){

					if (rsSalt.next()) {

						System.out.println("\nSalt = " + rsSalt.getBlob("salt_value").toString());
						return rsSalt.getBlob("salt_value");
					}

					else {
						DisplayMessage.showPanel("Errore nell'individuazione del sale nel DB. Riprova");
						return null;
					}
				}
			}

		}catch(ClassNotFoundException|SQLException e) {
			DisplayMessage.showPanel("Si è verificato un problema di connessione!");
			e.printStackTrace();
		}

		return null;
	}

}
