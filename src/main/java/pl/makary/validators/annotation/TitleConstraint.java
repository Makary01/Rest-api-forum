package pl.makary.validators.annotation;


import pl.makary.validators.TitleValidator;
import pl.makary.validators.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TitleValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleConstraint {
    String message() default "Title should be 5-100 charters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
