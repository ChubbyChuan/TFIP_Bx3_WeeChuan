package sg.edu.nus.iss.PTWServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.PTWServer.Model.User;
import sg.edu.nus.iss.PTWServer.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    public ResponseEntity<String> registerUser(User user) {
        try {
            return userRepo.createUser(user);
        } catch (Exception e) {
            System.out.print(e);
            return ResponseEntity.badRequest().body("User Registration Fail");
        }

    }

    public ResponseEntity<String> verifyUser(String email, String password) {
        try {
            User user = userRepo.retrieveUser(email);
            // System.out.println(password == user.getPassword()); this will show false as
            // it refernces to instance
            // System.out.println(password.equalsIgnoreCase(user.getPassword()));
            System.out.println("Password Received:" + password);
            System.out.println("Database Password:" + user.getPassword());
            if (password.equals(user.getPassword())) {

                // Create a Map to store the response data
                // Map<String, Object> response = new HashMap<>();
                // response.put("valid", true);
                // response.put("userdetails", user.toJson());
                // // Convert the Map to a JSON string
                // ObjectMapper objectMapper = new ObjectMapper();
                // String jsonString = objectMapper.writeValueAsString(response);
                String jsonString = user.toJson().toString();
                return ResponseEntity.ok(jsonString);
            }
            return ResponseEntity.badRequest().body("Invalid User or Password");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid Login" + e.getMessage());
        }
    }

}
