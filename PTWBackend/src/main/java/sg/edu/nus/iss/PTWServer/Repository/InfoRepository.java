package sg.edu.nus.iss.PTWServer.Repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Repository
public class InfoRepository {
    @Autowired
    private JdbcTemplate jdbctemplate;

    public static final String SQL_DATA_RETRIEVE_ALL_TYPE = "SELECT type, COUNT(*) AS count FROM Request WHERE status = ? GROUP BY type;";

    public ResponseEntity<String> sortbyAllType(String status) {
        try {
            SqlRowSet rowSet = jdbctemplate.queryForRowSet(SQL_DATA_RETRIEVE_ALL_TYPE, status);
            List<String> types = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();

            while (rowSet.next()) {
                String entryType = rowSet.getString("type");
                int count = rowSet.getInt("count");

                types.add(entryType);
                counts.add(count);
            }

            // Create a JSON object with the types and counts
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("type", Json.createArrayBuilder(types))
                    .add("data", Json.createArrayBuilder(counts))
                    .build();

            // Convert the JSON object to a string
            String jsonResult = jsonObject.toString();

            // Return the JSON response
            return ResponseEntity.ok(jsonResult);
        } catch (DataAccessException e) {
            System.out.print(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static final String SQL_DATA_RETRIEVE_TYPE = "SELECT locations, COUNT(*) AS count FROM Request WHERE type = ? AND status = ? GROUP BY locations;";

    // modify to sort by type specifically
    public ResponseEntity<String> sortbyTypewithLocation(String type, String status) {
        try {
            SqlRowSet rowSet = jdbctemplate.queryForRowSet(SQL_DATA_RETRIEVE_TYPE, type, status);
            List<String> locations = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();

            while (rowSet.next()) {
                String entryLocation = rowSet.getString("locations");
                int count = rowSet.getInt("count");

                locations.add(entryLocation);
                counts.add(count);
            }

            // Convert the locations and counts lists to JSON format
            // Create a JSON object with the types and counts
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("type", type)
                    .add("locations", Json.createArrayBuilder(locations))
                    .add("data", Json.createArrayBuilder(counts))
                    .build();

            // Convert the JSON object to a string
            String jsonResult = jsonObject.toString();

            // Return the JSON response
            return ResponseEntity.ok(jsonResult);
        } catch (DataAccessException e) {
            System.out.print(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static final String SQL_DATA_RETRIEVE_ALL_LOCATION = "SELECT locations, COUNT(*) AS count FROM Request WHERE status = ? GROUP BY locations;";

    public ResponseEntity<String> sortbyAllLocation(String status) {
        try {
            SqlRowSet rowSet = jdbctemplate.queryForRowSet(SQL_DATA_RETRIEVE_ALL_LOCATION, status);
            List<String> locations = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();

            while (rowSet.next()) {
                String entryLocation = rowSet.getString("locations");
                int count = rowSet.getInt("count");

                locations.add(entryLocation);
                counts.add(count);
            }

            // Create a JSON object with the locations and counts
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("location", Json.createArrayBuilder(locations))
                    .add("data", Json.createArrayBuilder(counts))
                    .build();

            // Convert the JSON object to a string
            String jsonResult = jsonObject.toString();

            // Return the JSON response
            return ResponseEntity.ok(jsonResult);
        } catch (DataAccessException e) {
            System.out.print(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static final String SQL_DATA_RETRIEVE_LOCATION = "SELECT type, COUNT(*) AS count FROM Request WHERE Locations = ?  AND status = ? GROUP BY type;";

    public ResponseEntity<String> sortbyLocationwithType(String location, String status) {
        try {
            SqlRowSet rowSet = jdbctemplate.queryForRowSet(SQL_DATA_RETRIEVE_LOCATION, location, status);
            List<String> types = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();

            while (rowSet.next()) {
                String entryType = rowSet.getString("type");
                int count = rowSet.getInt("count");

                types.add(entryType);
                counts.add(count);
            }

            // Create a JSON object with the locations and counts
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("location", location) // dataset
                    .add("type", Json.createArrayBuilder(types)) // label
                    .add("data", Json.createArrayBuilder(counts)) // data
                    .build();

            // Convert the JSON object to a string
            String jsonResult = jsonObject.toString();

            // Return the JSON response
            return ResponseEntity.ok(jsonResult);
        } catch (DataAccessException e) {
            System.out.print(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static final String SQL_DATA_RETRIEVE_PERMIT_NUMBER = "SELECT status, COUNT(*) as type_count FROM Request GROUP BY status;";

public ResponseEntity<String> getPermitNumber() {
    try {
        SqlRowSet rowSet = jdbctemplate.queryForRowSet(SQL_DATA_RETRIEVE_PERMIT_NUMBER);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        while (rowSet.next()) {
            String entryStatus = rowSet.getString("status");
            int count = rowSet.getInt("type_count");

            jsonObjectBuilder.add(entryStatus, count);
        }

        // Build the JSON object
        JsonObject jsonObject = jsonObjectBuilder.build();

        // Convert the JSON object to a string
        String jsonResult = jsonObject.toString();

        // Return the JSON response
        return ResponseEntity.ok(jsonResult);
    } catch (DataAccessException e) {
        System.out.print(e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
}


}
