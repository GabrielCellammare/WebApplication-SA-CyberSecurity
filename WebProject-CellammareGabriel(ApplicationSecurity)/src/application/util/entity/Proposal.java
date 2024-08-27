package application.util.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Dao.db.DatabaseConnection;
import model.Dao.db.DatabaseQuery;


public class Proposal {
	private String email;
	private String fileName;
	private String htmlContent;

	public Proposal(String email, String fileName, String htmlContent) {
		this.email = email;
		this.fileName = fileName;
		this.htmlContent = htmlContent;
	}

	public static List<Proposal> getProposals() {
		List<Proposal> proposals = new ArrayList<>();

		try (Connection connection_read = DatabaseConnection.getConnectionRead();
				PreparedStatement ps_proposal = connection_read.prepareStatement(DatabaseQuery.takeEmailAndProposal());
				ResultSet resultSet = ps_proposal.executeQuery()) {

			while (resultSet.next()) {
				String email = resultSet.getString("email");
				String fileName = resultSet.getString("proposal_name");
				String htmlContent = resultSet.getString("proposal_content");

				Proposal proposal = new Proposal(email, fileName, htmlContent);
				proposals.add(proposal);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return proposals;
	}

	public String getEmail() {
		return email;
	}

	public String getFileName() {
		return fileName;
	}

	public String getHtmlContent() {
		return htmlContent;
	}


}
