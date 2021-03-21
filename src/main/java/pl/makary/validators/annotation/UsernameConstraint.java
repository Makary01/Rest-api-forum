package pl.makary.validators.annotation;


import pl.makary.validators.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraint {
    String message() default "Username should be 4-20 letters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}