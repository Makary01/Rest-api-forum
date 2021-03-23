package pl.makary.model.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthorModel {
    private String username;
    private String email;
    private LocalDateTime created;
    private LocalDateTime lastOnline;
}
