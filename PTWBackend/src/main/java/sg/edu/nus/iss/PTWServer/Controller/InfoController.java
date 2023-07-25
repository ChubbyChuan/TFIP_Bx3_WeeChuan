package sg.edu.nus.iss.PTWServer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sg.edu.nus.iss.PTWServer.Repository.InfoRepository;
import sg.edu.nus.iss.PTWServer.Service.InfoService;

@Controller
@RequestMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class InfoController {

    @Autowired
    InfoRepository infoRepo;
    @Autowired
    InfoService infoSvc;

    @GetMapping(path = "/graphtype/{type}")
    @ResponseBody
    public ResponseEntity<String> getType(@PathVariable String type) {
        if (type.equalsIgnoreCase("all")) {
            return infoSvc.sortbyAllTypePending();
        } else {
            return infoSvc.pendingTypewithLocation(type);
        }
    }

    @GetMapping(path = "/graphlocation/{location}")
    @ResponseBody
    public ResponseEntity<String> getLocation(@PathVariable String location) {
        if (location.equalsIgnoreCase("all")) {
            return infoSvc.sortbyAllLocationPending();
        } else {
            return infoSvc.pendingbyLocationwithType(location);
        }
    }

    @GetMapping(path = "/graphlocation")
    @ResponseBody
    public ResponseEntity<String> allLocation() {
        return infoRepo.sortbyAllLocation("approved");
    }

    @GetMapping(path = "/number")
    @ResponseBody
    public ResponseEntity<String> permitnumber() {
        return infoRepo.getPermitNumber();
    }
}
