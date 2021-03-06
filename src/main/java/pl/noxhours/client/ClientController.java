package pl.noxhours.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.noxhours.case_.Case;
import pl.noxhours.case_.CaseService;
import pl.noxhours.rate.Rate;
import pl.noxhours.rate.RateService;
import pl.noxhours.timesheet.Timesheet;
import pl.noxhours.timesheet.TimesheetService;

import javax.validation.Valid;

@Log4j2
@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final RateService rateService;
    private final TimesheetService timesheetService;
    private final CaseService caseService;

    @RequestMapping("/list")
    public String listClients(Model model, @RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean all, @RequestParam(required = false) String sortName, @RequestParam(required = false) String sortType, @RequestParam(required = false) String search) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (all == null) {
            all = false;
        }
        if (sortName == null || !(sortName.equalsIgnoreCase("created") || sortName.equalsIgnoreCase("name"))) {
            sortName = "created";
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

        Sort sort = Sort.by(Sort.Direction.fromString(sortType), sortName);
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<Client> clientPage = search == null || search.isEmpty() ? clientService.findAll(pageable, all) : clientService.findAllSearch(pageable, search, all);

        if (page != 1 && page > clientPage.getTotalPages()) {
            page = clientPage.getTotalPages();
            pageable = PageRequest.of(page - 1, 10, sort);
            clientPage = search == null || search.isEmpty() ? clientService.findAll(pageable, all) : clientService.findAllSearch(pageable, search, all);
        }
        model.addAttribute("page", page);
        model.addAttribute("totalPages", clientPage.getTotalPages());
        model.addAttribute("clients", clientPage.getContent());
        return "/client/clientsList";
    }

    @GetMapping("/show/{client}")
    public String showClient(Model model, @PathVariable(required = false) Client client, @RequestParam(required = false) Integer ratePage, @RequestParam(required = false) Integer timesheetPage, @RequestParam(required = false) Integer casePage, @RequestParam(required = false) Boolean allCases) {

        if (client == null) {
            log.warn("user " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to access invalid Client entity");
            return "redirect:/clients/list";
        }

        model.addAttribute("client", client);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            if (ratePage == null || ratePage < 1) {
                ratePage = 1;
            }
            Page<Rate> rates = rateService.findAllByClient(PageRequest.of(ratePage - 1, 10, Sort.by(Sort.Direction.DESC, "dateFrom")), client);
            if (rates.getTotalPages() > ratePage && ratePage != 1) {
                ratePage = rates.getTotalPages();
                rates = rateService.findAllByClient(PageRequest.of(ratePage - 1, 10, Sort.by(Sort.Direction.DESC, "dateFrom")), client);
            }
            model.addAttribute("ratePage", ratePage);
            model.addAttribute("totalRatePages", rates.getTotalPages());
            model.addAttribute("rates", rates.getContent());
        }
        if (timesheetPage == null || timesheetPage < 1) {
            timesheetPage = 1;
        }
        Page<Timesheet> timesheets = timesheetService.findAll(PageRequest.of(timesheetPage - 1, 10, Sort.by(Sort.Direction.DESC, "dateExecuted")), client);
        if (timesheets.getTotalPages() < timesheetPage && timesheetPage != 1) {
            timesheetPage = timesheets.getTotalPages();
            timesheets = timesheetService.findAll(PageRequest.of(timesheetPage - 1, 10, Sort.by(Sort.Direction.DESC, "dateExecuted")), client);
        }
        timesheets.getContent().forEach(timesheetService::replaceUserWithDto);
        model.addAttribute("timesheetPage", timesheetPage);
        model.addAttribute("totalTimesheetPages", timesheets.getTotalPages());
        model.addAttribute("timesheets", timesheets.getContent());

        if (casePage == null || casePage < 1) {
            casePage = 1;
        }
        if (allCases == null) {
            allCases = false;
        }
        Page<Case> cases = allCases ? caseService.findAll(PageRequest.of(casePage - 1, 10, Sort.by(Sort.Direction.DESC, "created")), client) : caseService.findAllActive(PageRequest.of(casePage - 1, 10, Sort.by(Sort.Direction.DESC, "id")), client);

        if (cases.getTotalPages() < casePage && casePage != 1) {
            casePage = cases.getTotalPages();
            cases = allCases ? caseService.findAll(PageRequest.of(casePage - 1, 10, Sort.by(Sort.Direction.DESC, "created")), client) : caseService.findAllActive(PageRequest.of(casePage - 1, 10, Sort.by(Sort.Direction.DESC, "id")), client);
        }
        model.addAttribute("allCases", allCases);
        model.addAttribute("casePage", casePage);
        model.addAttribute("totalCasePages", cases.getTotalPages());
        model.addAttribute("cases", cases.getContent());
        return "/client/clientShow";
    }

    @PostMapping("/show/{clientNotUsed}")
    public String editClient(Model model, @Valid Client client, BindingResult bindingResult) {

        clientService.fillMissingFields(client);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("edit", true);
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
                Page<Rate> rates = rateService.findAllByClient(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateFrom")), client);
                model.addAttribute("ratePage", 1);
                model.addAttribute("totalRatePages", rates.getTotalPages());
                model.addAttribute("rates", rates.getContent());
            }

            Page<Timesheet> timesheets = timesheetService.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateExecuted")), client);
            timesheets.getContent().forEach(timesheetService::replaceUserWithDto);
            model.addAttribute("timesheetPage", 1);
            model.addAttribute("totalTimesheetPages", timesheets.getTotalPages());
            model.addAttribute("timesheets", timesheets.getContent());

            Page<Case> cases = caseService.findAllActive(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created")), client);
            model.addAttribute("casePage", 1);
            model.addAttribute("totalCasePages", cases.getTotalPages());
            model.addAttribute("cases", cases.getContent());

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
