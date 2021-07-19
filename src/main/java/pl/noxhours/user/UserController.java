package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.noxhours.activity.ActivityService;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.timesheet.TimesheetService;
import pl.noxhours.user.DTO.UserAdminListDTO;
import pl.noxhours.user.DTO.UserPasswordChangeDTO;
import pl.noxhours.user.DTO.UserSettingsDTO;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequiredArgsConstructor
@SessionAttributes({"loggedUserName", "loggedUserId", "loggedUserAdminStatus", "loggedUserSuperAdminStatus", "forceLogout"})
public class UserController {

    private final UserService userService;
    private final ActivityService activityService;
    private TimesheetService timesheetService;

    @Autowired
    public void setTimesheetService(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    //TODO Wprowadzić opcję checkboxa z zapamiętaniem użytkownika - pola logged_key i persistence_logging (ciacho z key czyszczone przy wylogowaniu) w bazie - czy trzeba wtedy POSTa Spring Security nadpisać?
    //TODO Wprowadzić reset hasła przez użytkownika
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

    //    TODO - Dodać to do success handlera w configu?
    @RequestMapping("/logging")
    public String logging(Model model) {
        model.addAttribute("loggedUserName", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getFullName());
        model.addAttribute("loggedUserId", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
        model.addAttribute("loggedUserAdminStatus", SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
        model.addAttribute("loggedUserSuperAdminStatus", SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN")));
        return "redirect:/dashboard";
    }

    @RequestMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activities", activityService.findRecent());
        model.addAttribute("recentCount", timesheetService.getRecentTimesheetCount());
        model.addAttribute("recentSum", timesheetService.getRecentTimesheetSum());
        return "dashboard";
    }

    @GetMapping("/settings/show")
    public String showSettings(Model model) {
        model.addAttribute("user", userService.userToUserSettingsDto(userService.read((Long) model.getAttribute("loggedUserId"))));
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
            return "user/settings";
        }
        if (user.getId() != model.getAttribute("loggedUserId")) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to modify details of user with id of " + user.getId());
            return "redirect:/dashboard";
        }
        model.addAttribute("loggedUserName", user.getFirstName() + " " + user.getLastName());
        userService.update(userService.userSettingsDtoToUser(user));

        if (!user.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            model.addAttribute("forceLogout", true);
        }
        return "redirect:/settings/show?editSuccess=true" + (!user.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ? "&logout=true" : "");
    }

    @GetMapping("/settings/changePassword")
    public String changePasswordSendToForm(Model model) {
        model.addAttribute("user", userService.userToUserPasswordChangeDto(userService.read(SecurityContextHolder.getContext().getAuthentication().getName())));
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
        userService.update(userService.userPasswordDtoToUser(user));
        return "redirect:/settings/show";
    }

    @RequestMapping("/admin/list")
    public String adminListUsers(Model model, @RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean all, @RequestParam(required = false) String sortName, @RequestParam(required = false) String sortType, @RequestParam(required = false) String search) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (all == null) {
            all = false;
        }
        if (sortName == null || !(sortName.equalsIgnoreCase("firstName") || sortName.equalsIgnoreCase("lastName") || sortName.equalsIgnoreCase("email") || sortName.equalsIgnoreCase("rank") || sortName.equalsIgnoreCase("isLocked"))) {
            sortName = "id";
        }
        if (sortType == null || !(sortType.equalsIgnoreCase("asc") || sortType.equalsIgnoreCase("desc"))) {
            sortType = "desc";
        }
        if (search != null) {
            search = search.replaceAll("\\s", "");
        }
        model.addAttribute("search", search);
        model.addAttribute("all", all);
        model.addAttribute("sortName", sortName.toLowerCase());
        model.addAttribute("sortType", sortType.toLowerCase());

        if (sortName.equalsIgnoreCase("firstname")) {
            sortName = "firstName";
        }
        if (sortName.equalsIgnoreCase("lastname")) {
            sortName = "lastName";
        }
        Sort sort = Sort.by(Sort.Direction.fromString(sortType), sortName);
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<User> users = search == null || search.isEmpty() ? userService.findAll(pageable, all) : userService.findAll(pageable, all, search);
        if (page > users.getTotalPages()) {
            page = users.getTotalPages();
            pageable = PageRequest.of(page - 1, 10, sort);
            users = search == null || search.isEmpty() ? userService.findAll(pageable, all) : userService.findAll(pageable, all, search);
        }
        model.addAttribute("page", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("users", users.getContent().stream().map(userService::userToUserAdminListDto).collect(Collectors.toList()));

        return "admin/userList";
    }

    @RequestMapping("admin/show/{user}")
    public String showUser(@PathVariable(required = false) User user, Model model) {
        if (user == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to display invalid user");
            return "redirect:/dashboard";
        }
        model.addAttribute("userPermission", userService.checkEditPermissionForAdmin(user));
        model.addAttribute("user", userService.userToUserAdminListDto(user));
        return "admin/userShow";

    }

    @GetMapping("admin/edit")
    public String redirectToList() {
        return "redirect:/admin/list";
    }

    @PostMapping("admin/edit")
    public String userEdit(@ModelAttribute("user") @Valid UserAdminListDTO user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("userPermission", userService.checkEditPermissionForAdmin(userService.userAdminListDtoToUser(user)));
            return "admin/userShow";
        }
        if (!userService.checkEditPermissionForAdmin(userService.userAdminListDtoToUser(user))) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to modify user with id of " + user.getId() + " without proper privilages");
            return "redirect:/dashboard";
        }
        if (Arrays.stream(user.getPrivileges()).collect(Collectors.toList()).contains("S") && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN"))) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to give to user with id " + user.getId() + " with Superadmin privileges without proper privileges");
        }
        userService.update(userService.userAdminListDtoToUser(user));

        if (model.getAttribute("loggedUserId") == user.getId() && !user.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            model.addAttribute("forceLogout", true);
        }
        return "redirect:/admin/show/" + user.getId() + "?editSuccess=true";
    }

    @RequestMapping("admin/delete/{user}")
    public String deleteUser(@PathVariable(required = false) User user, Model model) {
        if (user == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete invalid user");
            return "redirect:/dashboard";
        }
        if (!userService.checkEditPermissionForAdmin(user)) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete user with id of " + user.getId() + " without proper privileges");
            return "redirect:/dashboard";
        }
        userService.delete(user);
        if (user.getId() == model.getAttribute("loggedUserId")) {
            model.addAttribute("forceLogout", true);
        }
        return "redirect:/admin/list?deleteSuccess=true";
    }

    @GetMapping("/admin/add")
    public String addUserSendToForm(Model model) {
        model.addAttribute("user", new UserAdminListDTO());
        return "admin/userAdd";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("user") @Valid UserAdminListDTO user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/userAdd";
        }
        if (Arrays.stream(user.getPrivileges()).collect(Collectors.toList()).contains("S") && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN"))) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to add user with SuperAdmin privileges without proper privileges");
        }
        User completeUser = userService.userAdminListDtoToUser(user);
        completeUser.setPassword(new BCryptPasswordEncoder(10).encode(GlobalConstants.DEFAULT_PASSWORD));
        completeUser.setPasswordReset(false);
        userService.create(completeUser);
        return "redirect:/admin/list?addSuccess=true";
    }

}
