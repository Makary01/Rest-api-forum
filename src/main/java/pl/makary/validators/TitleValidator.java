package pl.makary.validators;

import pl.makary.validators.annotation.TitleConstraint;
import pl.makary.validators.annotation.UsernameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleValidator implements
        ConstraintValidator<TitleConstraint, String> {

    @Override
    public void initialize(TitleConstraint titleConstraint) {
    }

    @Override
    public boolean isValid(String title,
                           ConstraintValidatorContext cxt) {
        return title != null && title.matches(".{5,100}");
    }

}
