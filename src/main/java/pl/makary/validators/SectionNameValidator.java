package pl.makary.validators;

import pl.makary.validators.annotation.PasswordConstraint;
import pl.makary.validators.annotation.SectionNameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionNameValidator implements
        ConstraintValidator<SectionNameConstraint, String> {

    @Override
    public void initialize(SectionNameConstraint sectionNameConstraint) {
    }

    @Override
    public boolean isValid(String sectionName,
                           ConstraintValidatorContext cxt) {
        return sectionName != null && sectionName.matches(".{10,155}");
    }

}