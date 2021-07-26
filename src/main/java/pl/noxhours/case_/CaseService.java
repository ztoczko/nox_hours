package pl.noxhours.case_;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;
import pl.noxhours.report.ReportService;
import pl.noxhours.timesheet.TimesheetService;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CaseService {

    private final CaseRepository caseRepository;
    private final ReportService reportService;

    private TimesheetService timesheetService;

    @Autowired
    public void setTimesheetService(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    public void create(Case aCase) {
        if (aCase.getClosed() == null) {
            aCase.setClosed(false);
        }
        aCase.setCreated(LocalDateTime.now());
        caseRepository.save(aCase);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created case with id of " + aCase.getId());
    }

    public Case read(Long id) {
        return caseRepository.findById(id).orElse(null);
    }

    public void update(Case aCase) {
        if (aCase.getClosed() == null) {
            aCase.setClosed(false);
        }
        caseRepository.save(aCase);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated case with id of " + aCase.getId());
    }

    public void delete(Case aCase) {
        timesheetService.findAll(aCase).forEach(timesheetService::delete);
        reportService.findAll(aCase).forEach(reportService::delete);
        caseRepository.delete(aCase);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " deleted case with id of " + aCase.getId());
    }

    public List<Case> findAll(Client client) {
        return caseRepository.findAllByClientOrderByCreatedDesc(client);
    }

    public Page<Case> findAll(Pageable pageable, Client client) {
        return caseRepository.findAllByClient(pageable, client);
    }

    public List<Case> findAllActive(Client client) {
        return caseRepository.findAllByClientAndClosedOrderByCreatedDesc(client, false);
    }

    public Page<Case> findAllActive(Pageable pageable, Client client) {
        return caseRepository.findAllByClientAndClosed(pageable, client, false);
    }

    public List<Case> findAllActive() {
        return caseRepository.findAllByClosed(false);
    }

}
