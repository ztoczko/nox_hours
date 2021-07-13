package pl.noxhours.client;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.noxhours.NoxHoursApplication;
import pl.noxhours.rate.RateService;

import javax.validation.Valid;
import java.util.Locale;

@Log4j2
@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final RateService rateService;

    @RequestMapping("/list")
    public String listClients(Model model, @RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean all, @RequestParam(required = false) String sortName, @RequestParam(required = false) String sortType, String search) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (all == null) {
           all = false;
        }
        if (sortName == null || !(sortName.toLowerCase().equals("created") || sortName.toLowerCase().equals("name"))) {
            sortName = "created";
        }
        if (sortType == null || !(sortType.toLowerCase().equals("asc") || sortType.toLowerCase().equals("desc"))) {
            sortType = "desc";
        }
        model.addAttribute("search", search);
        model.addAttribute("all", all);
        model.addAttribute("sortName", sortName.toLowerCase());
        model.addAttribute("sortType", sortType.toLowerCase());

        Sort sort = Sort.by(Sort.Direction.fromString(sortType), sortName);
        Page<Client> clientPage = search == null || search.isEmpty() ? clientService.findAll(PageRequest.of(page - 1, 10, sort), all) : clientService.findAllSearch(PageRequest.of(page - 1, 10, sort), search, all);

        if (page != 1 && page > clientPage.getTotalPages()) {
            page = clientPage.getTotalPages();
            clientPage = search == null || search.isEmpty() ? clientService.findAll(PageRequest.of(page - 1, 10, sort), all) : clientService.findAllSearch(PageRequest.of(page - 1, 10, sort), search, all);
        }
        model.addAttribute("page", page);
        model.addAttribute("totalPages", clientPage.getTotalPages());
        model.addAttribute("clients", clientPage.getContent());
        return "/client/clientsList";
    }

    @RequestMapping("/show/{client}")
    public String showClient(Model model, @PathVariable(required = false) Client client) {

        if (client == null) {
            log.warn("user " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to access invalid Client entity");
            return "redirect:/clients/list";
        }
        //TODO Przekazać do widoku listę timesheetów
        //making sure object doesn't contain rate info - as client is not owner of the relation there is no risk in saving it to DB
        client.setRates(null);
        model.addAttribute("client", client);
//        System.out.println(client.getRates());
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            //displaying only last 10 rates
            //TODO rozbudować o pełną historię rate?
//            model.addAttribute("rates", client.getRates());
            model.addAttribute("rates", rateService.findAllByClient(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateTo")), client).getContent());
        }
        return "/client/clientShow";
    }

    @GetMapping("/edit")
    public String editGet() {
        return "redirect:/clients/list";
    }

    @PostMapping("/edit")
    public String editClient(Model model, @Valid Client client, BindingResult bindingResult) {

        clientService.fillMissingFields(client);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("edit", true);
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
                //displaying only last 10 rates
                //TODO rozbudować o pełną historię rate?
                model.addAttribute("rates", rateService.findAllByClient(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateTo")), client).getContent());
            }
            return "/client/clientShow";
        }
        clientService.update(client);
        return "redirect:/clients/show/" + client.getId() + "?editSuccess=true";
    }

    @RequestMapping("delete/{client}")
    public String deleteClient(@PathVariable(required = false) Client client) {
        if (client == null) {
            log.warn("user" + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete invalid Client entity");
            return "redirect:/clients/list";
        }
        clientService.delete(client);
        return "redirect:/clients/list?deleteSuccess=true";
    }

    @GetMapping("add")
    public String addClientSendToForm(Model model) {
        model.addAttribute("client", new Client());
        return "client/clientAdd";
    }

    @PostMapping("add")
    public String addClient(@Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/client/clientAdd";
        }
        clientService.create(client);
        return "redirect:/clients/list?addSuccess=true";
    }

}
