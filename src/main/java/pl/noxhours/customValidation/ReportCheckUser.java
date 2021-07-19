package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReportCheckUserValidator.class)
@Target( {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportCheckUser {

    String message() default "{pl.noxhours.customValidation.ReportCheckUser.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
