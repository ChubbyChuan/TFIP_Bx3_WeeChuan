package sg.edu.nus.iss.PTWServer.Repository;

import java.sql.PreparedStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.PTWServer.Model.*;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbctemplate;

    public static final String SQL_INSERT_USER_REQUEST = "INSERT INTO User (name, email, password, company) "
            + "VALUES (?, ?, ?, ?)";

    public ResponseEntity<String> createUser(User user) {
        try {
            String name = user.getName();
            String email = user.getEmail();
            String password = user.getPassword();
            String company = user.getCompany();

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbctemplate.update(connection -> {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER_REQUEST,
                            PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);
                    preparedStatement.setString(4, company);
                    return preparedStatement;
                } catch (Exception e) {
                    System.out.print(e);
                    throw new RuntimeException("Error creating PreparedStatement: " + e.getMessage(), e);
                }
            }, keyHolder);
            int generatedId = keyHolder.getKey().intValue();
            return ResponseEntity.ok("{\"Upload Successful. New Permid ID\": " + generatedId + "}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Saving Failed: " + e.getMessage());
        }

    }
    
    public static final String SQL_RETRIEVE_USER_BY_EMAIL = "SELECT * FROM User WHERE (email = ?);";

    public User retrieveUser(String email) {
        System.out.print(email);
        try {
            SqlRowSet rowSet = jdbctemplate.queryForRowSet(SQL_RETRIEVE_USER_BY_EMAIL, email);
            while (rowSet.next()) {
            User user = User.create(rowSet);
            System.out.println(user.toString());

            return user;
            }
        } catch (DataAccessException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

}
