package testDTO.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton Chernov on 2/2/2016.
 */
public class EmployeeDTO extends AccountDTO {
    private static Map<String, Long> rolesMap;

    static  {
        rolesMap = new HashMap<>();
        rolesMap.put("Admin", 2L);
        rolesMap.put("Dispatcher", 3L);
        rolesMap.put("Manager", 4L);
        rolesMap.put("Driver", 5L);
    }
    public String password;

    public String roles;


}
