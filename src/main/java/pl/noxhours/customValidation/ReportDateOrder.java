package pl.noxhours.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReportDateOrderValidator.class)
@Target( {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportDateOrder {

    String message() default "{pl.noxhours.customValidation.ReportDateOrder.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
