package pl.makary.exception;

import java.util.HashMap;
import java.util.Map;

public class IncorrectPasswordException extends Throwable{
    private final String message;
    private final String objectName;

    public IncorrectPasswordException(){
        this.message = "password incorrect";
        this.objectName = "password";
    }

    public String getObjectName() {
        return objectName;
    }

    public Map<String,String> getError(){
        Map map = new HashMap();
        map.put(objectName,getMessage());
        return map;
    }
}
