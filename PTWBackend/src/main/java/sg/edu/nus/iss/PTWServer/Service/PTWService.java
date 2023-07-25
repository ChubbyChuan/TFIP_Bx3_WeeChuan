package sg.edu.nus.iss.PTWServer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.PTWServer.Model.*;
import sg.edu.nus.iss.PTWServer.Repository.PTWRepository;

@Service
public class PTWService {

    @Autowired
    PTWRepository ptwRepo;

    // Save into Repo
    public ResponseEntity<String> saveRequest(Request request, String email) {

        return ptwRepo.saveRequest(request, email);
    }

    /* retrive Request */
    public ResponseEntity<String> retrieveRequestByFilters(String type, String location, String status) {
        try {
            List<Request> requestList = ptwRepo.retrieveRequestByFilters(type, location, status);
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Request request : requestList) {
                // System.out.print(request.toString());
                JsonObject jsonObject = request.toJson();
                jsonArrayBuilder.add(jsonObject);
            }

            JsonArray jsonArray = jsonArrayBuilder.build();
            String result = jsonArray.toString();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Retriving Error Filter");
        }

    }

        public ResponseEntity<String> retrieveRequestNoFilters(String status) {
        try {
            List<Request> requestList = ptwRepo.retrieveRequestNoFilters(status);
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Request request : requestList) {
                // System.out.print(request.toString());
                JsonObject jsonObject = request.toJson();
                jsonArrayBuilder.add(jsonObject);
            }

            JsonArray jsonArray = jsonArrayBuilder.build();
            String result = jsonArray.toString();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Retriving Error Filter");
        }

    }

    public ResponseEntity<String> retrieveRequestById(int id) {
        Request request = ptwRepo.retrieveRequestByid(id);
        if (request == null) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found response
        }

        try {
            String result = request.toJson().toString();
            // System.out.print(result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error Converting");
        }
    }

    // Update Request
    public ResponseEntity<String> updateRequest(Request request, String email) {
        return ptwRepo.updateRequest(request, email);
    }

    // Update Request
    public ResponseEntity<String> cancelRequest(int id, String name) {
        String status = "cancel";
        return ptwRepo.updateRequestStatus(id, status, name);
    }

    public ResponseEntity<String> closeRequest(int id, String name) {
        String status = "closed";
        return ptwRepo.updateRequestStatus(id, status, name);
    }

    public ResponseEntity<String> retrieveRequestall() {
        try {
            List<Request> requestList = ptwRepo.retrieveRequestNoFilters("pending");
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Request request : requestList) {
                // System.out.print(request.toString());
                JsonObject jsonObject = request.toJson();
                jsonArrayBuilder.add(jsonObject);
            }

            JsonArray jsonArray = jsonArrayBuilder.build();
            String result = jsonArray.toString();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Retriving Error Filter");
        }
    }

}
