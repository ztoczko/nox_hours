package pl.noxhours.customValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.noxhours.user.DTO.UserPasswordChangeDTO;
import pl.noxhours.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, UserPasswordChangeDTO> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(UserPasswordChangeDTO user, ConstraintValidatorContext context) {

        if (user == null || !new BCryptPasswordEncoder(10).matches(user.getOldPassword(), userService.read(user.getId()).getPassword())) {
            context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.CheckPassword.message}").addPropertyNode("oldPassword").addConstraintViolation();
            return false;
        }
        return true;
    }
}
