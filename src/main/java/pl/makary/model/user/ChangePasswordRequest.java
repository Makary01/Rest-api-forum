package pl.makary.model.user;

import lombok.Data;
import pl.makary.validators.annotation.PasswordConstraint;

@Data
public class ChangePasswordRequest {
    @PasswordConstraint
    private String oldPassword;
    @PasswordConstraint
    private String newPassword;
}
