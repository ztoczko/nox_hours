package pl.noxhours.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.noxhours.client.Client;
import pl.noxhours.client.ClientService;
import pl.noxhours.configuration.GlobalConstants;

import javax.validation.Valid;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/clients/{client}/rate")
public class RateController {

    private final RateService rateService;

    @ModelAttribute("dateFormat")
    public String dateFormat() {
        return GlobalConstants.DATE_FORMAT.replace("M", "m");
    }

    @GetMapping("/add")
    public String addRateSendToForm(@PathVariable(required = false) Client client, Model model) {
        if (client == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to edit rate for invalid Client entity");
            return "redirect:/clients/list";
        }
        Rate rate = new Rate();
        rate.setClient(client);
        model.addAttribute("rate", rate);
        return "rate/rateAdd";
    }

    @PostMapping("/add")
    public String addRate(@Valid Rate rate, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "rate/rateAdd";
        }

        rateService.create(rate);
        return "redirect:/clients/show/" + rate.getClient().getId() + "?rateAddSuccess=true";
    }

    @RequestMapping("/delete/{rate}")
    public String deleteRate(@PathVariable(required = false) Rate rate) {
        if (rate == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete invalid rate");
            return "redirect:/clients/list";
        }
        rateService.delete(rate);
        return "redirect:/clients/show/" + rate.getClient().getId() + "?rateDeleteSuccess=true";
    }


}
