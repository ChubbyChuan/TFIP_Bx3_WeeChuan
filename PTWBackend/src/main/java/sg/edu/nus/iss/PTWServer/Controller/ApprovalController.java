package sg.edu.nus.iss.PTWServer.Controller;

import java.io.StringReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.PTWServer.Repository.ApprovalRepository;
import sg.edu.nus.iss.PTWServer.Repository.PTWRepository;
import sg.edu.nus.iss.PTWServer.Service.EmailService;
import sg.edu.nus.iss.PTWServer.Service.PTWService;

@Controller
@RequestMapping(path = "/approval", produces = MediaType.APPLICATION_JSON_VALUE)
// @CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class ApprovalController {

    @Autowired
    ApprovalRepository approvalRepo;

    @Autowired
    PTWService ptwSvc;
    @Autowired
    PTWRepository ptwRepo;

    @Autowired
    EmailService email;

    @PostMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> updatePTW(@PathVariable("id") int id, @RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jsonObject = reader.readObject();
        reader.close();
    
        JsonArray selectedWorkArea = jsonObject.getJsonArray("selectedWorkArea");
        JsonArray selectedPPE = jsonObject.getJsonArray("selectedPPE");
        JsonArray selectedPrecaution = jsonObject.getJsonArray("selectedPrecaution");
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String company = jsonObject.getString("company");
    
        String selectedWorkAreaString = selectedWorkArea.toString();
        String selectedPPEString = selectedPPE.toString();
        String selectedPrecautionString = selectedPrecaution.toString();
    
        System.out.println(">>>>>> selectedWorkArea: " + selectedWorkAreaString);
        System.out.println(">>>>>> selectedPPE: " + selectedPPEString);
        System.out.println(">>>>>> selectedPrecaution: " + selectedPrecautionString);
        System.out.println(">>>>>> name: " + name);
        System.out.println(">>>>>> email: " + email);
        System.out.println(">>>>>> company: " + company);
        
        ResponseEntity<String> response = ptwRepo.updateRequestStatus(id, "approved", name);

        return approvalRepo.saveApproval(name, company,selectedWorkAreaString,selectedPPEString, selectedPrecautionString,  id, email);
    }

    @PostMapping(path = "/cancel/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> cancelPTW(@PathVariable("id") int id, @RequestBody String payload) {
        System.out.print("Cancellation received");
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();
        String name = req.getString("name");
        return ptwSvc.cancelRequest(id, name);
    }

}
