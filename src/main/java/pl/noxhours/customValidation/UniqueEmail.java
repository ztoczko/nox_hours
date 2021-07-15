package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "{pl.noxhours.customValidation.UniqueEmail.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
