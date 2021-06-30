package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loggedUserName")
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

    @RequestMapping("/dashboard")
    public String dashboard(Model model) {

        if (model.getAttribute("loggedUserName") == null) {
            model.addAttribute("loggedUserName", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getFullName());
        }

        return "dashboard";
    }


}