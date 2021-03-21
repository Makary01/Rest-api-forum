package pl.makary.model.user;

import lombok.Data;
import pl.makary.validators.annotation.PasswordConstraint;

import javax.validation.constraints.Email;

@Data
public class ChangeEmailRequest {
    @Email
    private String email;
    @PasswordConstraint
    private String password;
}
