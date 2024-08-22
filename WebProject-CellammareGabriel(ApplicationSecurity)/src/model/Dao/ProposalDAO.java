package model.Dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.util.customMessage.DisplayMessage;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class ProposalDAO {
	public static boolean uploadFile(String email, String filename,byte[] fileContent)
			throws IOException {


		Connection con_write = null;
		Connection con_read = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			con_write = DatabaseConnection.getConnectionWrite();
			con_read = DatabaseConnection.getConnectionRead();

			try (PreparedStatement ps_proposal = con_write.prepareStatement(DatabaseQuery.insertProposta())) {

				int id_user=0;
				try (PreparedStatement psIdSelect = con_read.prepareStatement(DatabaseQuery.registrationUserId())) {
					psIdSelect.setString(1, email);

					try (ResultSet rs = psIdSelect.executeQuery()) {
						if (rs.next()) {
							id_user=rs.getInt("id"); 
							System.out.println("\nID = " + id_user);
						}
					}

				}
				ps_proposal.setInt(1, id_user);
				ps_proposal.setString(2, filename);
				ps_proposal.setBytes(3, fileContent);

				int rowsAffected = ps_proposal.executeUpdate();

				return rowsAffected > 0;

			} catch (SQLException e) {
				DisplayMessage.showPanel("Si � verificato un errore durante l'inserimento della proposta!");
				e.printStackTrace();
			}
		} catch (ClassNotFoundException | SQLException e) {
			DisplayMessage.showPanel("Si � verificato un problema di connessione!");
			e.printStackTrace();
		} finally {
			// Chiusura della connessione nel blocco finally
			try {
				if (con_write != null) {
					con_write.close();
				}
				if (con_read != null) {
					con_read.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return false;
	}
}
