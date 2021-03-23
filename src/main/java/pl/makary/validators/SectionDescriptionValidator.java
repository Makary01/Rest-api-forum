package pl.makary.validators;

import pl.makary.validators.annotation.PasswordConstraint;
import pl.makary.validators.annotation.SectionDescriptionConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionDescriptionValidator implements
        ConstraintValidator<SectionDescriptionConstraint, String> {

    @Override
    public void initialize(SectionDescriptionConstraint sectionDescriptionConstraint) {
    }

    @Override
    public boolean isValid(String sectionDescription,
                           ConstraintValidatorContext cxt) {
        return sectionDescription != null && sectionDescription.matches(".{0,1024}");
    }

}