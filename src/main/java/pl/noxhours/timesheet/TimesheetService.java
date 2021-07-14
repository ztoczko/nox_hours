package pl.noxhours.timesheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;
import pl.noxhours.user.User;
import pl.noxhours.user.UserService;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final UserService userService;

    public void create(Timesheet timesheet) {
        timesheet.setCreated(LocalDateTime.now());
        timesheet.setRankWhenCreated(timesheet.getUser().getRank());
        timesheetRepository.save(timesheet);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created new timesheet with id of " + timesheet.getId() + " for client with id of " + timesheet.getClient().getId());
    }

    public Timesheet read(Long id) {
        return timesheetRepository.getById(id);
    }

    public void update(Timesheet timesheet) {
        if (!userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId().equals(timesheet.getUser().getId()) && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            log.error("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to update timesheet with id of " + timesheet.getId() + " while impersonating user with id " + timesheet.getUser().getId());
            return;
        }
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

    public Page<Timesheet> findAll(Pageable pageable, Client client) {
        return timesheetRepository.findAllByClient(pageable, client);
    }

    public void replaceUserWithDto(Timesheet timesheet) {
        timesheet.setUserNameDTO(userService.UserToUserNameDto(timesheet.getUser()));
        timesheet.setUser(new User(timesheet.getUserNameDTO().getId()));
    }

    public boolean checkPermissionForTimesheet(Timesheet timesheet) {
        return userService.read(SecurityContextHolder.getContext().getAuthentication().getName()).getId().equals(timesheet.getUser().getId()) || SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
    }

}
