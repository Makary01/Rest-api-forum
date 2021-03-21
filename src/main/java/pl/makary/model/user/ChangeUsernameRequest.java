package pl.makary.model.user;

import lombok.Data;
import pl.makary.validators.annotation.PasswordConstraint;
import pl.makary.validators.annotation.UsernameConstraint;

@Data
public class ChangeUsernameRequest {

    @UsernameConstraint
    private String username;
    @PasswordConstraint
    private String password;
}
