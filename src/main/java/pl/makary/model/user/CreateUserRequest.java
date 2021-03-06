package pl.makary.model.user;

import lombok.Data;
import pl.makary.validators.annotation.PasswordConstraint;
import pl.makary.validators.annotation.UsernameConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {
    @UsernameConstraint
    private String username;

    @PasswordConstraint
    private String password;

    @NotNull
    @Email(message = "Invalid email address")
    private String email;
}
