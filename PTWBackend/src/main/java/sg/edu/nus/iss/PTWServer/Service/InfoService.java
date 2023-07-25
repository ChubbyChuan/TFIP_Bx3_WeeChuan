package sg.edu.nus.iss.PTWServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.PTWServer.Repository.InfoRepository;

@Service
public class InfoService {

    @Autowired
    InfoRepository infoRepo;

    public ResponseEntity<String> sortbyAllTypePending() {
        return infoRepo.sortbyAllType("pending");
    }

    public ResponseEntity<String> pendingTypewithLocation(String type) {
        return infoRepo.sortbyTypewithLocation(type, "pending");
    }

    public ResponseEntity<String> sortbyAllLocationPending() {
        return infoRepo.sortbyAllLocation("pending");
    }

    public ResponseEntity<String> pendingbyLocationwithType(String location) {
        return infoRepo.sortbyLocationwithType(location, "pending");
    }

    
}
