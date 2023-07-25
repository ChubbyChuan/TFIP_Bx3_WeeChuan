package sg.edu.nus.iss.PTWServer.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.PTWServer.Model.User;
import sg.edu.nus.iss.PTWServer.Service.EmailService;

@Repository
public class TelegramRepository {

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Autowired
    EmailService emailSvc;

    @Autowired
    UserRepository userRepo;

    public static final String SQL_INSERT_REQUEST = "INSERT INTO Request (type, name, company, equipment, startdate, enddate, locations, comment, status, user_email) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public String requestFromTele(String type, String location, String equipment, String comments, String email,String startdateString, String enddateString, String password) {
		try {
            
            User user = userRepo.retrieveUser(email); 
            if (!user.getPassword().equalsIgnoreCase(password)){
                return "incorrect login";
            }
			String name = user.getName();
			String company = user.getCompany();

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
			DateTime startdate = formatter.parseDateTime(startdateString);
			DateTime enddate = formatter.parseDateTime(enddateString );

			String status = "pending";

			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbctemplate.update(connection -> {
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_REQUEST,
							PreparedStatement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, type);
					preparedStatement.setString(2, name);
					preparedStatement.setString(3, company);
					preparedStatement.setString(4, equipment);
					preparedStatement.setTimestamp(5, new Timestamp(startdate.getMillis()));
					preparedStatement.setTimestamp(6, new Timestamp(enddate.getMillis()));
					preparedStatement.setString(7, location);
					preparedStatement.setString(8, comments);
					preparedStatement.setString(9, status);
					preparedStatement.setString(10, email);
					return preparedStatement;
				} catch (Exception e) {
					throw new RuntimeException("Error creating PreparedStatement: " + e.getMessage(), e);
				}
			}, keyHolder);

			int generatedId = keyHolder.getKey().intValue();
			String reply = "New Permit ID: " + generatedId;
			String subject = "Hi! " + name + " from "+ company + ". Upload Successful. Id:" + generatedId;
			String text = "Here are the key information. type = " + type + "equipment: " + equipment + " @ " + location + "Details:" + comments ;

			emailSvc.sendSimpleMessage(email, subject, text);

			return reply;
		} catch (Exception e) {
			// Handle specific exceptions or provide a general error message
			return e.getMessage();
		}
	}
}
