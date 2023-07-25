package sg.edu.nus.iss.PTWServer.Model;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Request {
    private int id;
    private String type;
    private String name;
    private String company;
    private String equipment;
    private DateTime startdate;
    private DateTime enddate;
    private String[] location;
    private String comments;
    private String status;

    public Request() {
    }

    // To solve the comments null
    public Request(String type, String name, String company, String equipment, String startdate,
            String enddate, String location, String comments) {
        this.type = type;
        this.name = name;
        this.company = company;
        this.equipment = equipment;
        this.location = location.split(",");
        this.comments = comments;

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
        this.startdate = formatter.parseDateTime(startdate);
        this.enddate = formatter.parseDateTime(enddate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
    public DateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(DateTime startdate) {
        this.startdate = startdate;
    }

    public DateTime getEnddate() {
        return enddate;
    }

    public void setEnddate(DateTime enddate) {
        this.enddate = enddate;
    }

    public String[] getLocation() {
        return location;
    }

    public void setLocation(String[] location) {
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request [id=" + id + ", type=" + type + ", name=" + name + ", company=" + company + ", equipment="
                + equipment + ", startdate=" + startdate + ", enddate=" + enddate + ", location="
                + Arrays.toString(location) + ", comments=" + comments + ", status=" + status + "]";
    }

    // function to build jsonObject
    public JsonObject toJson() {
       
        /* To append location[] in jsonArray
        JsonArray result = null;
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (String x : this.getLocation())
            builder.add(x);
        result = builder.build();
        */
        
		StringBuilder sb = new StringBuilder();
		for (String location : this.getLocation())
			sb.append(location);
			// sb.append(location).append(",");
		

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
        String startTimeString = formatter.print(getStartdate());
        String endTimeString = formatter.print(getEnddate());

        return Json.createObjectBuilder()
                .add("id", getId())
                .add("type", getType())
                .add("name", getName())
                .add("company", getCompany())
                .add("equipment", getEquipment())
                .add("startdate", startTimeString)
                .add("enddate", endTimeString)
                .add("locations", sb.toString())
                .add("comment", getComments())
                .add("status", getStatus())
                .build();
    }


    // Create Request object from SqlRowSet
    public static Request create(SqlRowSet resultSet) {
        Request request = new Request();

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");

        request.setId(resultSet.getInt("id"));
        request.setType(resultSet.getString("type"));
        request.setName(resultSet.getString("name"));
        request.setCompany(resultSet.getString("company"));
        request.setEquipment(resultSet.getString("equipment"));

        // long startDatetimeMillis = resultSet.getTime("startdate").getTime();
        // DateTime startDatetime = new DateTime(startDatetimeMillis);
        // request.setStartdate(startDatetime);

        // long endDatetimeMillis = resultSet.getTime("enddate").getTime();
        // DateTime endDatetime = new DateTime(endDatetimeMillis);
        // request.setEnddate(endDatetime);

        DateTime startDatetime = new DateTime(resultSet.getTime("startdate").getTime());
        String startDatetimeString = formatter.print(startDatetime);
        DateTime startdate = formatter.parseDateTime(startDatetimeString);
        request.setStartdate(startdate);

        DateTime endDatetime = new DateTime(resultSet.getTime("enddate").getTime());
        String endDatetimeString = formatter.print(endDatetime);
        DateTime enddate = formatter.parseDateTime(endDatetimeString);
        request.setEnddate(enddate);

        String locationString = resultSet.getString("locations");
        request.setLocation(locationString.split(","));

        request.setComments(resultSet.getString("comment"));
        request.setStatus(resultSet.getString("status"));

        return request;
    }

}
