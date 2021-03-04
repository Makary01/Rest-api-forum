package pl.makary.model;

import lombok.Data;

@Data
public class AuthenticationResponse {

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    private String jwt;
}
