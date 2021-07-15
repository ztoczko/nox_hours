package pl.noxhours.customValidation;

import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.user.DTO.UserPasswordResetDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NewPasswordValidator implements ConstraintValidator<NewPassword, UserPasswordResetDTO> {

    @Override
    public boolean isValid(UserPasswordResetDTO userPasswordResetDTO, ConstraintValidatorContext context) {

        boolean isValid = true;
        if (userPasswordResetDTO.getNewPassword() == null || userPasswordResetDTO.getNewPasswordRepeated() == null || !userPasswordResetDTO.getNewPassword().equals(userPasswordResetDTO.getNewPasswordRepeated())) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.PasswordsDontMatch.message}").addPropertyNode("newPassword").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.PasswordsDontMatch.message}").addPropertyNode("newPasswordRepeated").addConstraintViolation();
            isValid = false;
        }
        if (isValid) {
            for (String matcher : GlobalConstants.PASSWORD_MATCHERS) {
                if (!userPasswordResetDTO.getNewPassword().matches(matcher)) {
                    isValid = false;
                    context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.NewPassword.message}").addPropertyNode("newPassword").addConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.NewPassword.message}").addPropertyNode("newPasswordRepeated").addConstraintViolation();
                    break;
                }
            }
        }

        return isValid;
    }
}
