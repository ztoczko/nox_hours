package pl.noxhours.customValidation;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import pl.noxhours.user.DTO.AbstractUserEmail;
import pl.noxhours.user.User;
import pl.noxhours.user.UserRepository;
import pl.noxhours.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, AbstractUserEmail> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(AbstractUserEmail validatedUser, ConstraintValidatorContext context) {

        User user = userService.read(validatedUser.getEmail());
        if (user == null || user.getId().equals(validatedUser.getId())) {
            return true;
        }
        context.buildConstraintViolationWithTemplate("{pl.noxhours.customValidation.UniqueEmail.message}").addPropertyNode("email").addConstraintViolation();
        return false;
    }
}
