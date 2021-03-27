package pl.makary.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ErrorResponse {
    private String msg;
    String error;
    Integer status;

    public ErrorResponse(String msg) {
        this.msg = msg;
    }


    public Map<String,Object> getBody(){
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("status",status);
        body.put("error",error);
        body.put("msg",msg);
        return body;
    }
}
