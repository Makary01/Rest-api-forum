package pl.makary.model;

import lombok.Data;

@Data
public class OkResponse {

    private Integer status = 200;
    private String message;

    public OkResponse(String message) {
        this.message = message;
    }
}
