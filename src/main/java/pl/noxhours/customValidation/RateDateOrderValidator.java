package pl.noxhours.customValidation;

import pl.noxhours.rate.Rate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RateDateOrderValidator implements ConstraintValidator<RateDateOrder, Rate> {


    public boolean isValid(Rate rate, ConstraintValidatorContext context) {

        boolean isValid = (rate.getRateNotExpires() != null && rate.getRateNotExpires()) || (rate.getDateTo() != null && !rate.getDateFrom().isAfter(rate.getDateTo()));

        if (!isValid) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.RateDateOrder.message}").addPropertyNode("dateTo").addConstraintViolation();
        }

        return isValid;
    }
}
