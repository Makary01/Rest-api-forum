package pl.makary.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {
    @NotNull
    @Size(min = 4,max = 16, message = "Username have to be 4 to 16 charters long")
    private String username;

    @NotNull
    @Size(min = 5, max=60, message = "Password have to be 5 to 60 charters long")
    private String password;

    @NotNull
    @Email
    private String email;
}
