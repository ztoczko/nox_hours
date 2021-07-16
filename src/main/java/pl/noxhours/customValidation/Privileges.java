package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PrivilegesValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Privileges {

    String message() default "{pl.noxhours.customValidation.Privileges.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
