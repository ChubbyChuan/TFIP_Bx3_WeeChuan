package sg.edu.nus.iss.PTWServer.Repository;

import org.joda.time.DateTime;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.PTWServer.Model.*;
import sg.edu.nus.iss.PTWServer.Service.EmailService;

@Repository
public class PTWRepository {

	@Autowired
	private JdbcTemplate jbdctemplate;

	@Autowired
	EmailService emailSvc;

	public static final String SQL_INSERT_REQUEST = "INSERT INTO Request (type, name, company, equipment, startdate, enddate, locations, comment, status, user_email) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public ResponseEntity<String> saveRequest(Request request, String email) {
		try {
			String type = request.getType();
			String name = request.getName();
			String company = request.getCompany();
			String equipment = request.getEquipment();
			String[] location = request.getLocation();
			String locationString = buildLocationString(location);
			String comments = request.getComments();

			DateTime startdate = request.getStartdate();
			DateTime enddate = request.getEnddate();

			String status = "pending";

			KeyHolder keyHolder = new GeneratedKeyHolder();
			jbdctemplate.update(connection -> {
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_REQUEST,
							PreparedStatement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, type);
					preparedStatement.setString(2, name);
					preparedStatement.setString(3, company);
					preparedStatement.setString(4, equipment);
					preparedStatement.setTimestamp(5, new Timestamp(startdate.getMillis()));
					preparedStatement.setTimestamp(6, new Timestamp(enddate.getMillis()));
					preparedStatement.setString(7, locationString);
					preparedStatement.setString(8, comments);
					preparedStatement.setString(9, status);
					preparedStatement.setString(10, email);
					return preparedStatement;
				} catch (Exception e) {
					throw new RuntimeException("Error creating PreparedStatement: " + e.getMessage(), e);
				}
			}, keyHolder);

			int generatedId = keyHolder.getKey().intValue();
			System.out.println("New Permit ID: " + generatedId);
			String subject = "Hi! " + name + " from " + company + ". Upload Successful. Id:" + generatedId;
			String text = "Here are the key information. type = " + type + "equipment: " + equipment + " @ "
					+ locationString + "Details:" + comments;

			emailSvc.sendSimpleMessage(email, subject, text);

			// return ResponseEntity.ok(String.valueOf(generatedId)); as a integer
			// return ResponseEntity.ok("{\"id\": \"number nine\"}"); as a string
			// return ResponseEntity.ok("{\"id\": \"number " + generatedId + "\"}");
			// combined both string and integer
			return ResponseEntity.ok("{\"Upload Successful. New Permid ID\": " + generatedId + "}");
		} catch (Exception e) {
			// Handle specific exceptions or provide a general error message
			return ResponseEntity.badRequest().body("Saving Failed: " + e.getMessage());
		}
	}

	/*------------------------------Search by Type, location, status---------------------------------------------- */

	public static final String SQL_GET_REQUEST_WITH_FILTERS = """
			SELECT * FROM Request WHERE (status = ?) AND (? is NULL or type = ?) AND(? is NULL or
			locations = ?)
			""";

	public List<Request> retrieveRequestByFilters(String type, String location,
			String status) {
		List<Request> requestList = new ArrayList<>();
		SqlRowSet resultSet = jbdctemplate.queryForRowSet(SQL_GET_REQUEST_WITH_FILTERS,
				status, type, type, location, location);

		while (resultSet.next())
			requestList.add(Request.create(resultSet));
		return requestList;
	}

	public static final String SQL_GET_REQUEST_NO_FILTERS = "SELECT * FROM Request WHERE (status = ?);";

	public List<Request> retrieveRequestNoFilters(String status) {
		List<Request> requestList = new ArrayList<>();
		SqlRowSet resultSet = jbdctemplate.queryForRowSet(SQL_GET_REQUEST_NO_FILTERS,
				status);
		while (resultSet.next())
			requestList.add(Request.create(resultSet));
		return requestList;
	}

	/*------------------------------Search by id---------------------------------------------- */

	public static final String SQL_GET_REQUEST_ID = "SELECT * FROM Request where (id = ?)";

	public Request retrieveRequestByid(int id) {
		// System.out.printf("id received: " + id); // make a note for invalid cursor
		try {
			SqlRowSet resultSet = jbdctemplate.queryForRowSet(SQL_GET_REQUEST_ID, id);
			if (resultSet.next()) {
				Request request = Request.create(resultSet);
				// System.out.printf(request.toString());
				return request;
			} else {
				// Handle the case when no rows are returned for the given id
				System.out.println("No rows found for id: " + id);
				return null; // Or throw an exception, return a default value, etc.
			}
		} catch (Exception e) {
			System.out.println("Error occurred during SQL query execution: " + e.getMessage());
			throw e; // Or handle the exception as per your requirements
		}
	}

	/*------------------------------------Update Request by ID------------------------------------------------------- */
	public static final String SQL_UPDATE_REQUEST = "UPDATE Request SET type = ?, name = ?, company = ?, equipment = ?, startdate = ?, enddate = ?, locations = ?, comment = ? WHERE id = ?";

	public ResponseEntity<String> updateRequest(Request request, String email) {
		try {
			int id = request.getId();
			String type = request.getType();
			String name = request.getName();
			String company = request.getCompany();
			String equipment = request.getEquipment();
			String[] location = request.getLocation();
			String locationString = buildLocationString(location);
			String comments = request.getComments();

			DateTime startdate = request.getStartdate();
			DateTime enddate = request.getEnddate();

			/* to check status of the ID before its approved */
			Request requestdatabase = retrieveRequestByid(id);
			if (!"pending".equalsIgnoreCase(requestdatabase.getStatus())) {
				return ResponseEntity.badRequest().body("The request has already been approved");
			}
			if (!name.equalsIgnoreCase(requestdatabase.getName())) {
				return ResponseEntity.badRequest().body("You cannot edit other people request");
			}
			String status = request.getStatus();

			jbdctemplate.update(SQL_UPDATE_REQUEST, type, name, company, equipment,
					new Timestamp(startdate.getMillis()), new Timestamp(enddate.getMillis()), locationString, comments,
					id);

			return ResponseEntity.ok("{\"Upload Successful for ID\": " + id + "}");
		} catch (

		Exception e) {
			// Handle specific exceptions or provide a general error message
			return ResponseEntity.badRequest().body("Update Failed: " + e.getMessage());
		}
	}

	/*------------------------------------Update status by ID------------------------------------------------------- */

	public static final String SQL_UPDATE_REQUEST_STATUS = "UPDATE Request SET status = ? WHERE id = ?";

	public ResponseEntity<String> updateRequestStatus(int id, String status, String name) {

		try {
			jbdctemplate.update(SQL_UPDATE_REQUEST_STATUS, status, id);
			return ResponseEntity.ok("{\"Cancellation Successful for ID\": " + id + "}");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("{\"Cancel Fail for ID\": " + id + "}");
		}
	}


	// Functions---------------------------------------------------------------------------------------------------------------

	private String buildLocationString(String[] location) {
		StringBuilder sb = new StringBuilder();
		for (String loc : location) {
			sb.append(loc).append(",");
		}
		// Remove the trailing comma if necessary
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
}
