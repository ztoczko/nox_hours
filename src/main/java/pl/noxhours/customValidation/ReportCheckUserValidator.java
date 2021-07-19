package pl.noxhours.customValidation;

import pl.noxhours.report.Report;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReportCheckUserValidator implements ConstraintValidator<ReportCheckUser, Report> {


    public boolean isValid(Report report, ConstraintValidatorContext context) {

        if (report.getBasedOnUser() == null || !report.getBasedOnUser()) {
            return true;
        }

        if (report.getBaseUser() == null && report.getBaseUserDTO() == null) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.ReportCheckUser.message}").addPropertyNode("baseUser").addConstraintViolation();
            return false;
        }

        return true;
    }
}
