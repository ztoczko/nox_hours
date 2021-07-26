package pl.noxhours.customValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PrivilegesValidator implements ConstraintValidator<Privileges, String[]> {
    @Override
    public boolean isValid(String[] privileges, ConstraintValidatorContext constraintValidatorContext) {
        if (privileges == null || privileges.length == 0) {
            return true;
        }
        String privilegesStr = new String();
        for (String str : privileges) {
            privilegesStr += str;
        }
        return privilegesStr.matches("[URAS]+");
    }
}
