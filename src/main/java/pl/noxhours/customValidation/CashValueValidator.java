package pl.noxhours.customValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashValueValidator implements ConstraintValidator<CashValue, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal cashValue, ConstraintValidatorContext constraintValidatorContext) {

        return cashValue != null && cashValue.compareTo(BigDecimal.valueOf(0)) != -1 &&
                cashValue.setScale(2, RoundingMode.CEILING).compareTo(cashValue) == 0;
    }
}
