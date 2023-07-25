package sg.edu.nus.iss.PTWServer.Controller;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import sg.edu.nus.iss.PTWServer.Model.User;
import sg.edu.nus.iss.PTWServer.Repository.UserRepository;
import sg.edu.nus.iss.PTWServer.Service.UserService;

@Controller
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userSvc;
    @Autowired
    UserRepository userRepo;


    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();

        String name = req.getString("name");
        String email = req.getString("email");
        String password = req.getString("password");
        String company = req.getString("company");

        System.out.printf(">>>>>> name %s%n", name);
        System.out.printf(">>>>>> email %s%n", email);
        System.out.printf(">>>>>> password %s%n", password);
        System.out.printf(">>>>>> company %s%n", company);

        User user = new User(name, email, password, company);

        return userSvc.registerUser(user);

    }

    @PostMapping(path = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> verifyUser(@RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();

        String email = req.getString("email");
        String password = req.getString("password");
        try {
            System.out.printf(">>>>>> email %s%n", email);
            System.out.printf(">>>>>> password %s%n", password);

            return userSvc.verifyUser(email, password);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("invalid User Request Json");
        }

    }
}
