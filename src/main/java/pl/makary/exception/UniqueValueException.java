package pl.makary.exception;

import org.hibernate.mapping.Collection;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UniqueValueException extends Exception{

    private String objectName;

    public UniqueValueException(String objectName, String message ){
        super(message);
        this.objectName = objectName;
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
