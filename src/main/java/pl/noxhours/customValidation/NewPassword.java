package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NewPasswordValidator.class)
@Target( {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NewPassword {

    String message() default "{pl.noxhours.customValidation.NewPassword.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
