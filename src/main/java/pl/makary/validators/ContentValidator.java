package pl.makary.validators;

import pl.makary.validators.annotation.ContentConstraint;
import pl.makary.validators.annotation.PasswordConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContentValidator implements
        ConstraintValidator<ContentConstraint, String> {

    @Override
    public void initialize(ContentConstraint contentConstraint) {
    }

    @Override
    public boolean isValid(String content,
                           ConstraintValidatorContext cxt) {
        return content != null && content.matches(".{30,10000}");
    }

}