package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckTimesheetTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTimesheetTime {

    String message() default "{pl.noxhours.customValidation.CheckTimesheetTime.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
