package model.Dao.registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.concurrent.Immutable;

import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseQuery;
@Immutable
final class UserAlreadyExistDAO {
	/**
	 * Metodo che controlla l'esistenza di un utente con la stessa mail
	 * @param con_read
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	static int userAlreadyExists(Connection con_read, String email) throws SQLException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (PreparedStatement ps = con_read.prepareStatement(DatabaseQuery.userAlreadyExist())) {
				ps.setString(1, email);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("count"); 
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
