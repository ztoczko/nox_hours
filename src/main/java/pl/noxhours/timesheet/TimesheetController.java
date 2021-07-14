package pl.noxhours.timesheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.noxhours.client.Client;
import pl.noxhours.client.ClientService;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.user.DTO.UserNameDTO;
import pl.noxhours.user.User;
import pl.noxhours.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/timesheet")
public class TimesheetController {

    private final TimesheetService timesheetService;
    private final UserService userService;
    private final ClientService clientService;

    @ModelAttribute("clients")
    public List<Client> getActiveClients() {
        return clientService.findAllActive();
    }

    @ModelAttribute("dateFormat")
    public String dateFormat() {
        return GlobalConstants.DATE_FORMAT.replace("M", "m");
    }

    @GetMapping("/add")
    public String addTimesheetSendToForm(Model model, @RequestParam(required = false) Client client) {
        if (client != null && client.getClosed()) {
            client = null;
        }
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            model.addAttribute("users", userService.findAllActive().stream().map(userService::UserToUserNameDto).collect(Collectors.toList()));
        }
        model.addAttribute("client", client);
        model.addAttribute("userId", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
//        model.addAttribute("userRank", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getRank());
        Timesheet timesheet = new Timesheet();
        timesheet.setUser(new User(userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId()));
        model.addAttribute("timesheet", timesheet);
        return "timesheet/timesheetAdd";
    }

    @PostMapping("/add")
    public String addTimesheet(@Valid Timesheet timesheet, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
//            model.addAttribute("userRank", userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getRank());
            if (timesheet.getClient() != null) {
                model.addAttribute("client", timesheet.getClient());
            }
            //avoiding sending sensitive user data - user will be binded by id after next form submission
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                model.addAttribute("users", userService.findAllActive().stream().map(userService::UserToUserNameDto).collect(Collectors.toList()));
            }
            if (timesheet.getUser() != null) {
                timesheetService.replaceUserWithDto(timesheet);
            }
            return "timesheet/timesheetAdd";
        }
        if (!timesheetService.checkPermissionForTimesheet(timesheet)) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to create timesheet while impersonating user with id " + timesheet.getUser().getId());
            return "redirect:/dashboard";
        }
        timesheetService.create(timesheet);
        return "redirect:/clients/show/" + timesheet.getClient().getId() + "?timesheetAddSuccess=true";
    }

    @RequestMapping("/client/{client}/show/{timesheet}")
    public String showTimesheetDetails(Model model, @PathVariable(required = false) Client client, @PathVariable(required = false) Timesheet timesheet) {
        if (client == null || client.getClosed() || timesheet == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to display invalid timesheet");
            return "redirect:/dashboard";
        }
        model.addAttribute("timesheetPermission", timesheetService.checkPermissionForTimesheet(timesheet));
        timesheetService.replaceUserWithDto(timesheet);
        model.addAttribute("timesheet", timesheet);
        return "timesheet/timesheetShow";
    }

    @RequestMapping("/delete/{timesheet}")
    public String deleteTimesheet(@PathVariable(required = false) Timesheet timesheet) {
        if (timesheet == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete invalid timesheet");
            return "redirect:/dashboard";
        }
        if (!timesheetService.checkPermissionForTimesheet(timesheet)) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete timesheet with id of " + timesheet.getId() + " while impersonating user with id " + timesheet.getUser().getId());
            return "redirect:/dashboard";
        }
        timesheetService.delete(timesheet);
        return "redirect:/clients/show/" + timesheet.getClient().getId() + "?timesheetDeleteSuccess=true";
    }

    @GetMapping("/edit")
    public String editGetRedirect() {
        return "redirect:/dashboard";
    }

    @PostMapping("/edit")
    public String timesheetEdit(@Valid Timesheet timesheet, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("timesheetPermission", timesheetService.checkPermissionForTimesheet(timesheet));
            timesheetService.replaceUserWithDto(timesheet);
            model.addAttribute("timesheet", timesheet);
            model.addAttribute("edit", true);
            return "timesheet/timesheetShow";
        }
        if (!timesheetService.checkPermissionForTimesheet(timesheet)) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to update timesheet with id of " + timesheet.getId() + " while impersonating user with id " + timesheet.getUser().getId());
            return "redirect:/dashboard";
        }
        timesheetService.update(timesheet);
        return "redirect:/timesheet/client/" + timesheet.getClient().getId() + "/show/" + timesheet.getId() + "?timesheetEditSuccess=true";
    }

}
