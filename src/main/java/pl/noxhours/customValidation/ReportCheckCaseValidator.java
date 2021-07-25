package pl.noxhours.customValidation;

import pl.noxhours.report.Report;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReportCheckCaseValidator implements ConstraintValidator<ReportCheckCase, Report> {


    public boolean isValid(Report report, ConstraintValidatorContext context) {

        if (report.getBasedOnCase() == null || !report.getBasedOnCase() || !report.getBasedOnClient()) {
            return true;
        }

        if (report.getBaseCase() == null) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.ReportCheckCase.message}").addPropertyNode("baseCase").addConstraintViolation();
            return false;
        }

        return true;
    }
}
