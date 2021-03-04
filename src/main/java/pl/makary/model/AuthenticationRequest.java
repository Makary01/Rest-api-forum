package pl.makary.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;

}
