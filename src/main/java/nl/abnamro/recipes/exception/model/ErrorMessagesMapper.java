package nl.abnamro.recipes.exception.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesMapper {
    private static final Map<String, ErrorMessages> errorMessagesHashMap;

    static {
        HashMap<String, ErrorMessages> map = new HashMap<>();
        for (ErrorMessages errorMessage : ErrorMessages.values()) {
            map.put(errorMessage.name(), errorMessage);
        }
        errorMessagesHashMap = Collections.unmodifiableMap(map);
    }

    public static ErrorMessages get(String key) {
        return errorMessagesHashMap.get(key);
    }
}
