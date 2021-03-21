package pl.makary.model.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadUserResponse {
    private String username;
    private String email;
    private LocalDateTime created;
    private LocalDateTime lastOnline;
}
