package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.noxhours.user.DTO.UserPasswordChangeDTO;
import pl.noxhours.user.DTO.UserSettingsDTO;

import javax.validation.Valid;

@Log4j2
@Controller
@RequiredArgsConstructor
@SessionAttributes({"loggedUserName", "loggedUserId", "loggedUserAdminStatus", "forceLogout"})
public class UserController {

    private final UserService userService;

    //TODO Wprowadzić opcję checkboxa z zapamiętaniem użytkownika - pola logged_key i persistence_logging (ciacho z key czyszczone przy wylogowaniu) w bazie - czy trzeba wtedy POSTa Spring Security nadpisać?
//    @GetMapping("/login")
//    public String login() {
//        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("USER")) {
//            return "redirect:/dashboard";
//        }
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String loginVerification() {
//        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("USER")) {
//            return "redirect:/dashboard";
//        }
//        return "login";
//    }

    //TODO dodanie strony startowej?
    @RequestMapping("/")
    public String mainPage() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("USER")) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @RequestMapping("/logging")
    public String logging(Model model) {
        model.addAttribute("loggedUserName", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getFullName());
        model.addAttribute("loggedUserId", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
        model.addAttribute("loggedUserAdminStatus", SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
        return "redirect:/dashboard";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/settings/show")
    public String showSettings(Model model) {
        model.addAttribute("user", userService.UserToUserSettingsDto(userService.read((Long) model.getAttribute("loggedUserId"))));
        return "user/settings";
    }

    @PostMapping("/settings/show")
    public String changeUserSettings(@ModelAttribute("user") @Valid UserSettingsDTO user, BindingResult result, Model model) {

//        BindingResult result = new BeanPropertyBindingResult(userSettingsDTO, "user");
//        validator.validate(userSettingsDTO, result);
//        System.out.println("PATH: " + result.getObjectName());
        if (result.hasErrors()) {
//            model.addAttribute("org.springframework.validation.BindingResult.user", result);
            model.addAttribute("edit", true);
            model.addAttribute("user", user);
            System.out.println(model.asMap());
            return "user/settings";
        }
        if (user.getId() != model.getAttribute("loggedUserId")) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to modify details of user with id of " + user.getId());
            return "redirect:/dashboard";
        }
        model.addAttribute("loggedUserName", user.getFirstName() + " " + user.getLastName());
        userService.update(userService.UserSettingsDtoToUser(user));
        if (!user.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {

//            TODO zrobić filtr dla force logout
            model.addAttribute("forceLogout", true);
        }
        return "redirect:/settings/show?editSuccess=true" + (!user.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ? "&logout=true" : "");
    }
//TODO dodać passwordDTO

    @GetMapping("/settings/changePassword")
    public String changePasswordSendToForm(Model model) {
        model.addAttribute("user", userService.UserToUserPasswordChangeDto(userService.read(SecurityContextHolder.getContext().getAuthentication().getName())));
        return "user/changePassword";
    }

    @PostMapping("/settings/changePassword")
    public String changePassword(@ModelAttribute("user") @Valid UserPasswordChangeDTO user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/changePassword";
        }
        if (user.getId() != model.getAttribute("loggedUserId")) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to modify password of user with id of " + user.getId());
            return "redirect:/dashboard";
        }
        userService.update(userService.UserPasswordDtoToUser(user));
        return "redirect:/settings/show";
    }

}
