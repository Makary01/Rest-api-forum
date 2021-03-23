package pl.makary.validators.annotation;

import pl.makary.validators.PasswordValidator;
import pl.makary.validators.SectionDescriptionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SectionDescriptionValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SectionDescriptionConstraint {
    String message() default "Description should be less than 1024 charters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
