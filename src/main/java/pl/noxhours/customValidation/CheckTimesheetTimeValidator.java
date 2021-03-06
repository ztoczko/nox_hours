package pl.noxhours.customValidation;

import pl.noxhours.timesheet.Timesheet;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckTimesheetTimeValidator implements ConstraintValidator<CheckTimesheetTime, Timesheet> {

    @Override
    public boolean isValid(Timesheet timesheet, ConstraintValidatorContext context) {

        boolean isValid = true;
        if (timesheet.getHours() == null || timesheet.getMinutes() == null) {
            isValid = false;
        }
        if (isValid && timesheet.getHours() == 0 && timesheet.getMinutes() == 0) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.CheckTimesheetTime.message}").addPropertyNode("hours").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.CheckTimesheetTime.message}").addPropertyNode("minutes").addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }
}
