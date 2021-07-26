package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RateDateOrderValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateDateOrder {

    String message() default "{pl.noxhours.customValidation.RateDateOrder.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
