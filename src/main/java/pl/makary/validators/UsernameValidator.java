package pl.makary.validators;

import pl.makary.validators.annotation.PasswordConstraint;
import pl.makary.validators.annotation.UsernameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements
        ConstraintValidator<UsernameConstraint, String> {

    @Override
    public void initialize(UsernameConstraint passwordConstraint) {
    }

    @Override
    public boolean isValid(String username,
                           ConstraintValidatorContext cxt) {
        return username != null && username.matches("^(?=[a-zA-Z0-9._]{4,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

}
