package pl.noxhours.customValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.noxhours.user.DTO.UserPasswordChangeDTO;
import pl.noxhours.user.DTO.UserPasswordResetMailDTO;
import pl.noxhours.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckResetKeyValidator implements ConstraintValidator<CheckResetKey, UserPasswordResetMailDTO> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(UserPasswordResetMailDTO user, ConstraintValidatorContext context) {

        if (userService.read(user.getEmail()) != null && userService.read(user.getEmail()).getPasswordResetKey().equals(user.getResetKey())) {
            return true;
        }
        context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.CheckResetKey.message}").addPropertyNode("email").addConstraintViolation();
        return false;
    }
}
