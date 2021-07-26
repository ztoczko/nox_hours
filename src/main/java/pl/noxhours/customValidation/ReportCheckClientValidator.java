package pl.noxhours.customValidation;

import pl.noxhours.report.Report;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReportCheckClientValidator implements ConstraintValidator<ReportCheckClient, Report> {

    public boolean isValid(Report report, ConstraintValidatorContext context) {

        if (report.getBasedOnClient() == null || !report.getBasedOnClient()) {
            return true;
        }
        if (report.getBaseClient() == null) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.ReportCheckClient.message}").addPropertyNode("baseClient").addConstraintViolation();
            return false;
        }
        return true;
    }
}
