package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReportCheckCaseValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportCheckCase {

    String message() default "{pl.noxhours.customValidation.ReportCheckCase.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
