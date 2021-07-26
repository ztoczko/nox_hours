package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckResetKeyValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckResetKey {

    String message() default "{pl.noxhours.customValidation.CheckResetKey.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
