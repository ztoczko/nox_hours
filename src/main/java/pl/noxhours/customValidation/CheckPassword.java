package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckPasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPassword {

    String message() default "{pl.noxhours.customValidation.CheckPassword.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
