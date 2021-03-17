package pl.makary.exception;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class ValidationException extends Throwable{

    String objectName;
    String message;

    public ResponseEntity<?> generateErrorResponse(){
        Map map = new HashMap();
        map.put(objectName,message);

        return ResponseEntity
                .badRequest()
                .body(map);
    }
}
