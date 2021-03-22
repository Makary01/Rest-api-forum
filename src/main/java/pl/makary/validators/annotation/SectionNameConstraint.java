package pl.makary.validators.annotation;

import pl.makary.validators.ContentValidator;
import pl.makary.validators.SectionNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SectionNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SectionNameConstraint {
    String message() default "Section's name should be 10-155 charters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
