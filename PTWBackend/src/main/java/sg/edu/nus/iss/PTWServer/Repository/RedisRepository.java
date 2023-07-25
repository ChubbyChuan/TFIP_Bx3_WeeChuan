package sg.edu.nus.iss.PTWServer.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

//refer to revision in module 2
@Repository
public class RedisRepository {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> template;

    // this is used to create the hashmap
    public void createUserStateDatabase(String id) {
        template.opsForHash().put(id, "state", "START"); // Set initial state as "START"
    }

    public void saveState(String id, String state) {
        template.opsForHash().put(id, "state", state);
    }

    public void saveType(String id, String type) {
        template.opsForHash().put(id, "type", type);
    }

    public void saveEquipment(String id, String equipment) {
        template.opsForHash().put(id, "equipment", equipment);
    }

    public void saveLocation(String id, String location) {
        template.opsForHash().put(id, "location", location);
    }

    public void saveComments(String id, String comments) {
        template.opsForHash().put(id, "comments", comments);
    }

    public String retrieveState(String id) {
        Object stateObject = template.opsForHash().get(id, "state");
        if (stateObject != null) {
            return stateObject.toString();
        }
        return null;
    }

    public String retrieveType(String id) {
        Object typeObject = template.opsForHash().get(id, "type");
        if (typeObject != null) {
            return typeObject.toString();
        }
        return null;
    }

    public String retrieveEquipment(String id) {
        Object equipmentObject = template.opsForHash().get(id, "equipment");
        if (equipmentObject != null) {
            return equipmentObject.toString();
        }
        return null;
    }

    public String retrieveLocation(String id) {
        Object locationObject = template.opsForHash().get(id, "location");
        if (locationObject != null) {
            return locationObject.toString();
        }
        return null;
    }

    public String retrieveComments(String id) {
        Object commentsObject = template.opsForHash().get(id, "comments");
        if (commentsObject != null) {
            return commentsObject.toString();
        }
        return null;
    }

    public void clearData(String id) {
        template.delete(id);
    }

    public void saveEmail(String id, String email) {
        template.opsForHash().put(id, "email", email);
    }

    public void savePassword(String id, String password) {
        template.opsForHash().put(id, "password", password);
    }

    public String retrieveEmail(String id) {
        Object locationObject = template.opsForHash().get(id, "email");
        if (locationObject != null) {
            return locationObject.toString();
        }
        return null;
    }

    public String retrievePassword(String id) {
        Object locationObject = template.opsForHash().get(id, "password");
        if (locationObject != null) {
            return locationObject.toString();
        }
        return null;
    }

    public void saveStartDateTime(String id, String dateTime) {
        template.opsForHash().put(id, "startDateTime", dateTime);
    }

    public void saveEndDateTime(String id, String dateTime) {
        template.opsForHash().put(id, "endDateTime", dateTime);
    }

    public String retrieveStartDateTime(String id) {
        Object startDateTimeObject = template.opsForHash().get(id, "startDateTime");
        if (startDateTimeObject != null) {
            return startDateTimeObject.toString();
        }
        return null;
    }

    public String retrieveEndDateTime(String id) {
        Object endDateTimeObject = template.opsForHash().get(id, "endDateTime");
        if (endDateTimeObject != null) {
            return endDateTimeObject.toString();
        }
        return null;
    }

}
