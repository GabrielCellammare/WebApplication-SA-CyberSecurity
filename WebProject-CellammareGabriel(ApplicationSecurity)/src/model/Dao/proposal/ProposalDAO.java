package model.Dao.proposal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import application.util.customMessage.DisplayMessage;
import model.Dao.TakeUserIdDAO;
import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;

public class ProposalDAO {
	public static boolean uploadFile(String email, String filename,byte[] fileContent)
			throws IOException {


		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try(Connection con_write = DatabaseConnection.getConnectionWrite();Connection con_read = DatabaseConnection.getConnectionRead()){

				try (PreparedStatement ps_proposal = con_write.prepareStatement(DatabaseQuery.insertProposal())) {

					int id_user=TakeUserIdDAO.takeUserId(con_read, email);
					ps_proposal.setInt(1, id_user);
					ps_proposal.setString(2, filename);
					ps_proposal.setBytes(3, fileContent);

					int rowsAffected = ps_proposal.executeUpdate();

					return rowsAffected > 0;

				} 



			}
		} catch (ClassNotFoundException | SQLException e) {
			DisplayMessage.showPanel("Si � verificato un errore durante l'inserimento della proposta!");
			e.printStackTrace();
		}


		return false;

	}
}