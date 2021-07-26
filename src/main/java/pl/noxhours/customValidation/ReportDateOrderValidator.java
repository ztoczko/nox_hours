package pl.noxhours.customValidation;

import pl.noxhours.report.Report;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReportDateOrderValidator implements ConstraintValidator<ReportDateOrder, Report> {

    public boolean isValid(Report report, ConstraintValidatorContext context) {

        if (report.getDateFrom() == null || report.getDateTo() == null || report.getDateTo().isBefore(report.getDateFrom())) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.ReportDateOrder.message}").addPropertyNode("dateFrom").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.ReportDateOrder.message}").addPropertyNode("dateTo").addConstraintViolation();
            return false;
        }
        return true;
    }
}
