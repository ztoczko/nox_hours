package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReportCheckClientValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportCheckClient {

    String message() default "{pl.noxhours.customValidation.ReportCheckClient.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
