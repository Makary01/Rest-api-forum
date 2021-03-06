package pl.makary.model.user;

import lombok.Data;
import pl.makary.validators.annotation.PasswordConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DeleteUserRequest {

    @PasswordConstraint
    private String password;
}
