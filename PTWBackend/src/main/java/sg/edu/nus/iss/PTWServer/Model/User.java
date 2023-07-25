package sg.edu.nus.iss.PTWServer.Model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {
    private String name;
    private String email;
    private String password;
    private String company;

    public User() {
    }


    public User(String name, String email, String password, String company) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.company = company;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User [name=" + name + ", email=" + email + ", password=" + password + ", company=" + company + "]";
    }



    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("name", getName())
                .add("email", getEmail())
                .add("password", getPassword())
                .add("company", getCompany())
                .build();
    }

    public static User create(SqlRowSet resultSet) {
        User user = new User();

        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setCompany(resultSet.getString("company"));

        return user;
    }



    public String getCompany() {
        return company;
    }



    public void setCompany(String company) {
        this.company = company;
    }

    
}
