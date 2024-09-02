package model.Dao.registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Dao.db.DatabaseQuery;

class RegistrationUserSaltDAO {
	static int registrationUserSalt(Connection con_write, int id_user, byte[] salt) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (PreparedStatement psSalt = con_write.prepareStatement(DatabaseQuery.registrationUserSaltQuery())) {
				psSalt.setInt(1, id_user);

				psSalt.setBytes(2, salt);

				return psSalt.executeUpdate();

			}

		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace(); 
		}
		return 0;
	}

}