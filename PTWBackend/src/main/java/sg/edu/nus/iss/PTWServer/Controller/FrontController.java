package sg.edu.nus.iss.PTWServer.Controller;

import java.io.StringReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telegram.telegrambots.meta.api.objects.Update;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.mail.MessagingException;
import sg.edu.nus.iss.PTWServer.Model.*;
import sg.edu.nus.iss.PTWServer.Repository.PTWRepository;
import sg.edu.nus.iss.PTWServer.Service.EmailService;
import sg.edu.nus.iss.PTWServer.Service.PTWBot;
import sg.edu.nus.iss.PTWServer.Service.PTWService;

@Controller
@RequestMapping(path = "/permit", produces = MediaType.APPLICATION_JSON_VALUE)
// @CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class FrontController {

    @Autowired
    PTWRepository ptwRepo;

    @Autowired
    PTWService ptwSvc;

    @Autowired
    private PTWBot ptwBot;

    @Autowired
    EmailService email;

    @RequestMapping("/telegram/webhook")
    public ResponseEntity<String> handleTelegramUpdate(@RequestBody Update update) throws MessagingException {
        ptwBot.onUpdateReceived(update);
        return ResponseEntity.ok("");
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createPTW(@RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();

        String type = req.getString("type");
        String name = req.getString("name");
        String company = req.getString("company");
        String equipment = req.getString("equipment");
        String startdate = req.getString("startdate");
        String enddate = req.getString("enddate");
        String location = req.getString("location");
        String comments = req.getString("comment");
        String email = req.getString("email");

        System.out.printf(">>>>>> Create type %s%n", type);
        System.out.printf(">>>>>> name %s%n", name);
        System.out.printf(">>>>>> company %s%n", company);
        System.out.printf(">>>>>> equipment %s%n", equipment);
        System.out.printf(">>>>>> starttime %s%n", startdate);
        System.out.printf(">>>>>> endtime %s%n", enddate);
        System.out.printf(">>>>>> location %s%n", location);
        System.out.printf(">>>>>> comments %s%n", comments);
        System.out.printf(">>>>>> comments %s%n", email);

        try {
            Request request = new Request(type, name, company, equipment, startdate, enddate, location, comments);
            // System.out.print(request.toString());
            return ptwSvc.saveRequest(request, email);
            // return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON payload");
        }
    }

    @GetMapping(path = "/search")
    @ResponseBody
    public ResponseEntity<String> searchPTW(
            @RequestParam("type") String type,
            @RequestParam("locations") String location,
            @RequestParam("status") String status) {

        return ptwSvc.retrieveRequestByFilters(type, location, status);
    }

    @GetMapping(path = "/searchall")
    @ResponseBody
    public ResponseEntity<String> searchPTW() {
        return ptwSvc.retrieveRequestall();
    }


    @GetMapping(path = "/search/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> searchPTWbyID(@PathVariable("id") String idString) {
        int id = Integer.parseInt(idString);

        return ptwSvc.retrieveRequestById(id);
    }

    @PostMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> updatePTW(@PathVariable("id") int id, @RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();

        String type = req.getString("type");
        String name = req.getString("name");
        String company = req.getString("company");
        String equipment = req.getString("equipment");
        String startdate = req.getString("startdate");
        String enddate = req.getString("enddate");
        String location = req.getString("location");
        String comments = req.getString("comment");
        String email = req.getString("email");

        System.out.printf(">>>>>> Edit id %s%n", id);
        System.out.printf(">>>>>> type %s%n", type);
        System.out.printf(">>>>>> name %s%n", name);
        System.out.printf(">>>>>> company %s%n", company);
        System.out.printf(">>>>>> equipment %s%n", equipment);
        System.out.printf(">>>>>> starttime %s%n", startdate);
        System.out.printf(">>>>>> endtime %s%n", enddate);
        System.out.printf(">>>>>> location %s%n", location);
        System.out.printf(">>>>>> comments %s%n", comments);
        System.out.printf(">>>>>> comments %s%n", email);

        try {
            Request request = new Request(type, name, company, equipment, startdate,
                    enddate, location, comments);
            request.setId(id);

            return ptwSvc.updateRequest(request, email);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON payload");
        }
    }

    @PostMapping(path = "/cancel/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> cancelPTW(@PathVariable("id") int id, @RequestBody String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();
        String name = req.getString("name");
        System.out.printf(">>>>>> comments %s%n", name);
        // return null;
        return ptwSvc.cancelRequest(id, name);
    }

    @PostMapping(path = "/close/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> closePTW(@PathVariable("id") int id, @RequestBody String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();
        String name = req.getString("name");
        System.out.printf(">>>>>> comments %s%n", name);
        // return null;
        return ptwSvc.closeRequest(id, name);
    }
}
