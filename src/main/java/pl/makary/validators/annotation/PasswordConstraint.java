package pl.makary.validators.annotation;

import pl.makary.validators.PasswordValidator;
import pl.makary.validators.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default "Password should be 5-60 charters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
