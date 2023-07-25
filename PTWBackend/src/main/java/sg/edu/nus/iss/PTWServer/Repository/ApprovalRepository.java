package sg.edu.nus.iss.PTWServer.Repository;

import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.PTWServer.Model.Request;
import sg.edu.nus.iss.PTWServer.Service.PTWService;

@Repository
public class ApprovalRepository {
	@Autowired
	private JdbcTemplate jbdctemplate;

	@Autowired
	PTWRepository ptwRepo;

	public static final String SQL_INSERT_APPROVAL = "INSERT INTO Approval (name, company, workarea, ppe, precaution, PTW_Id, Approval_email)"
			+ "VALUES (?,?,?,?,?,?,?)";

	public ResponseEntity<String> saveApproval(String name, String company, String workarea, String ppe,
			String precaution, int ptw_id, String email) {
		try {
			Request request = ptwRepo.retrieveRequestByid(ptw_id);
			if (name.equalsIgnoreCase(request.getName())) {
				return ResponseEntity.badRequest().body("You cannot approved your own request");
			}
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jbdctemplate.update(connection -> {
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_APPROVAL,
							PreparedStatement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, name);
					preparedStatement.setString(2, company);
					preparedStatement.setString(3, workarea);
					preparedStatement.setString(4, ppe);
					preparedStatement.setString(5, precaution);
					preparedStatement.setInt(6, ptw_id);
					preparedStatement.setString(7, email);
					return preparedStatement;
				} catch (Exception e) {
					throw new RuntimeException("Error creating PreparedStatement: " + e.getMessage(), e);
				}
			}, keyHolder);

			int generatedId = keyHolder.getKey().intValue();
			return ResponseEntity.ok("{\"Approved Successful. New Approval ID\": " + generatedId + "}");
		} catch (Exception e) {
			// Handle specific exceptions or provide a general error message
			return ResponseEntity.badRequest().body("Saving Failed: " + e.getMessage());
		}
	}
}
