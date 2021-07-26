package pl.noxhours.case_;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.noxhours.client.Client;
import pl.noxhours.timesheet.TimesheetService;

import javax.validation.Valid;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/clients/{client}/case")
public class CaseController {

    private final CaseService caseService;

    @GetMapping("/show/{aCase}")
    public String showCase(@PathVariable(required = false) Client client, @PathVariable(required = false) Case aCase, Model model) {

        if (client == null || aCase == null || !aCase.getClient().getId().equals(client.getId())) {
            log.warn("user" + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to display invalid Case entity");
            return "redirect:/dashboard";
        }
        model.addAttribute("aCase", aCase);
        return "case/caseShow";
    }

    @PostMapping("/show/{aCase}")
    public String getCaseForm(@ModelAttribute("aCase") @Valid Case aCase, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "case/caseShow";
        }
        if (!aCase.getClient().getId().equals(caseService.read(aCase.getId()).getClient().getId())) {
            log.warn("user" + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to change client for Case with id of " + aCase.getId());
            return "redirect:/dashboard";
        }

        caseService.update(aCase);
        return "redirect:/clients/" + aCase.getClient().getId() + "/case/show/" + aCase.getId() + "?editSuccess=true";
    }

    @GetMapping("/add")
    public String sendToCaseAddForm(@PathVariable(required = false) Client client, Model model) {

        if (client == null || client.getClosed()) {
            log.warn("user" + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to add invalid Case entity");
            return "redirect:/dashboard";
        }
        model.addAttribute("aCase", new Case(client));
        return "case/caseAdd";
    }

    @PostMapping("/add")
    public String caseAdd(@PathVariable(required = false) Client client, @ModelAttribute("aCase") @Valid Case aCase, BindingResult bindingResult, Model model) {

        if (client == null || client.getClosed()) {
            log.warn("user" + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to add invalid Case entity");
            return "redirect:/dashboard";
        }

        if (bindingResult.hasErrors()) {
            return "case/caseAdd";
        }

        if (!aCase.getClient().getId().equals(client.getId())) {
            log.warn("user" + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to add invalid Case entity");
            return "redirect:/dashboard";
        }
        caseService.create(aCase);
        return "redirect:/clients/show/" + client.getId() + "?caseAddSuccess=true&showCases=true";
    }

    @RequestMapping("/delete/{acase}")
    public String caseDelete(@PathVariable(required = false) Client client, @PathVariable(required = false) Case acase) {
        if (client == null || acase == null || !acase.getClient().getId().equals(client.getId())) {
            log.warn("user" + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete invalid Case entity");
            return "redirect:/dashboard";
        }
        caseService.delete(acase);
        return "redirect:/clients/show/" + client.getId() + "?caseDeleteSuccess=true&showCases=true";
    }
}
