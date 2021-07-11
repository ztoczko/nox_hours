package pl.noxhours.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    //TODO sortowanie list
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
}
