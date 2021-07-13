package pl.noxhours.timesheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;

    public void create(Timesheet timesheet) {
        timesheet.setCreated(LocalDateTime.now());
        timesheetRepository.save(timesheet);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created new timesheet with id of " + timesheet.getId() + " for client with id of " + timesheet.getClient().getId());
    }

    public Timesheet read(Long id) {
        return timesheetRepository.getById(id);
    }

    public void update(Timesheet timesheet) {
        timesheetRepository.save(timesheet);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated timesheet with id of " + timesheet.getId() + " for client with id of " + timesheet.getClient().getId());

    }

    public void delete(Timesheet timesheet) {
        timesheetRepository.delete(timesheet);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created deleted timesheet with id of " + timesheet.getId() + " for client with id of " + timesheet.getClient().getId());
    }

    public List<Timesheet> findAll(Client client) {
        return timesheetRepository.findAllByClient(client);
    }

}
