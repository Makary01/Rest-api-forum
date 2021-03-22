package pl.makary.validators.annotation;

import pl.makary.validators.ContentValidator;
import pl.makary.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContentValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentConstraint {
    String message() default "Content should be 30-10000 charters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
