package pl.noxhours.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.activity.ActivityService;
import pl.noxhours.rate.RateService;
import pl.noxhours.report.ReportService;
import pl.noxhours.timesheet.TimesheetService;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final RateService rateService;
    private final TimesheetService timesheetService;
    private final ReportService reportService;
    private final ActivityService activityService;

    public void create(Client client) {
        client.setCreated(LocalDateTime.now());
        if (client.getClosed() == null) {
            client.setClosed(false);
        }
        clientRepository.save(client);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created client with id of " + client.getId());
    }

    public Client read(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void update(Client client) {
        if (client.getClosed() == null) {
            client.setClosed(false);
        }
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated client with id of " + client.getId());
        clientRepository.save(client);
    }

    public void delete(Client client) {
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " deleted client with id of " + client.getId());
        rateService.findAllByClient(client).forEach(rateService::delete);
        timesheetService.findAll(client).forEach(timesheetService::delete);
        reportService.findAll(client).forEach(reportService::delete);
        activityService.findAll(client).forEach(activityService::delete);
        clientRepository.delete(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public List<Client> findAllActive() {
        return clientRepository.findAllByClosedOrderByCreatedDesc(false);
    }

    public Page<Client> findAll(Pageable pageable, boolean all) {
        return all ? clientRepository.findAllBy(pageable) : clientRepository.findAllByClosed(pageable, false);
    }

    public Page<Client> findAllSearch(Pageable pageable, String search, boolean all) {
        return all ? clientRepository.findAllByNameContains(pageable, search) : clientRepository.findAllByClosedAndNameContains(pageable, false, search);
    }

    public void fillMissingFields(Client client) {
        Client originalClient = read(client.getId());
        client.setCreated(originalClient.getCreated());
//        if (client.getRatesSet() == null) {
//            client.setRatesSet(originalClient.getRatesSet());
//        }
        if (client.getClosed() == null) {
            client.setClosed(false);
        }
    }
}
