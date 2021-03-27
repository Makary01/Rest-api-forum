package pl.makary.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class NotFoundResponse extends ErrorResponse{

    private String msg;

    public NotFoundResponse(String msg) {
        super(msg);
        this.error = "Not found";
        this.status = 404;
    }


}
