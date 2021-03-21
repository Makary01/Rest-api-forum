package pl.makary.validators;

import pl.makary.validators.annotation.PasswordConstraint;
import pl.makary.validators.annotation.UsernameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements
        ConstraintValidator<PasswordConstraint, String> {

    @Override
    public void initialize(PasswordConstraint usernameConstraint) {
    }

    @Override
    public boolean isValid(String password,
                           ConstraintValidatorContext cxt) {
        return password != null && password.matches("\\S{5,60}");
    }

}
