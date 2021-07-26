package pl.noxhours.timesheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.activity.Activity;
import pl.noxhours.activity.ActivityService;
import pl.noxhours.case_.Case;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.user.User;
import pl.noxhours.user.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final UserService userService;
    private final ActivityService activityService;

    public void create(Timesheet timesheet) {
        timesheet.setCreated(LocalDateTime.now());
        timesheet.setRankWhenCreated(timesheet.getUser().getRank());
        timesheetRepository.save(timesheet);
        activityService.create(new Activity(null, LocalDateTime.now(), timesheet.getUser().getFullName(), GlobalConstants.ADDED_TIMESHEET_MSG, timesheet.getClient()));
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created new timesheet with id of " + timesheet.getId() + " for client with id of " + timesheet.getClient().getId());
    }

    public Timesheet read(Long id) {
        return timesheetRepository.findById(id).orElse(null);
    }

    public void update(Timesheet timesheet) {
        if (!userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId().equals(timesheet.getUser().getId()) && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            log.error("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to update timesheet with id of " + timesheet.getId() + " while impersonating user with id " + timesheet.getUser().getId());
            return;
        }
        timesheetRepository.save(timesheet);
        activityService.create(new Activity(null, LocalDateTime.now(), timesheet.getUser().getFullName(), GlobalConstants.EDITED_TIMESHEET_MSG, timesheet.getClient()));
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated timesheet with id of " + timesheet.getId() + " for client with id of " + timesheet.getClient().getId());

    }

    public void delete(Timesheet timesheet) {
        timesheetRepository.delete(timesheet);
        activityService.create(new Activity(null, LocalDateTime.now(), timesheet.getUser().getFullName(), GlobalConstants.DELETED_TIMESHEET_MSG, timesheet.getClient()));
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created deleted timesheet with id of " + timesheet.getId() + " for client with id of " + timesheet.getClient().getId());
    }

    public List<Timesheet> findAll(Client client) {
        return timesheetRepository.findAllByClient(client);
    }

    public List<Timesheet> findAll(Case clientCase) {
        return timesheetRepository.findAllByClientCase(clientCase);
    }

    public Page<Timesheet> findAll(Pageable pageable, Client client) {
        return timesheetRepository.findAllByClient(pageable, client);
    }

    public List<Timesheet> findAll(User user) {
        return timesheetRepository.findAllByUser(user);
    }

    public List<Timesheet> findAll(LocalDate dateFrom, LocalDate dateTo) {
        return timesheetRepository.findAllByDateExecutedBetween(dateFrom, dateTo);
    }

    public List<Timesheet> findAll(User user, LocalDate dateFrom, LocalDate dateTo) {
        return timesheetRepository.findAllByUserAndDateExecutedBetween(user, dateFrom, dateTo);
    }

    public List<Timesheet> findAll(Client client, LocalDate dateFrom, LocalDate dateTo) {
        return timesheetRepository.findAllByClientAndDateExecutedBetween(client, dateFrom, dateTo);
    }

    public List<Timesheet> findAll(Client client, Case aCase, LocalDate dateFrom, LocalDate dateTo) {
        return timesheetRepository.findAllByClientAndClientCaseAndDateExecutedBetween(client, aCase, dateFrom, dateTo);
    }

    public List<Timesheet> findAll(User user, Client client, LocalDate dateFrom, LocalDate dateTo) {
        return timesheetRepository.findAllByUserAndClientAndDateExecutedBetween(user, client, dateFrom, dateTo);
    }

    public List<Timesheet> findAll(User user, Client client, Case aCase, LocalDate dateFrom, LocalDate dateTo) {
        return timesheetRepository.findAllByUserAndClientAndClientCaseAndDateExecutedBetween(user, client, aCase, dateFrom, dateTo);
    }

    public Integer getRecentTimesheetSum() {
        return timesheetRepository.getSumOfRecent(userService.read(SecurityContextHolder.getContext().getAuthentication().getName()), LocalDateTime.now().minusDays(30));
    }

    public Integer getRecentTimesheetCount() {
        return timesheetRepository.countByUserAndCreatedAfter(userService.read(SecurityContextHolder.getContext().getAuthentication().getName()), LocalDateTime.now().minusDays(30));
    }

    public void replaceUserWithDto(Timesheet timesheet) {
        timesheet.setUserNameDTO(userService.userToUserNameDto(timesheet.getUser()));
        timesheet.setUser(new User(timesheet.getUserNameDTO().getId()));
    }

    public boolean checkPermissionForTimesheet(Timesheet timesheet) {
        return userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId().equals(timesheet.getUser().getId()) || SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
    }

}
