package pl.makary.model;

import lombok.Data;


public class NotFoundResponse {
    private String msg;

    public NotFoundResponse(String msg) {
        this.msg = msg;
    }
}
