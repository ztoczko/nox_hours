package pl.noxhours.report;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;
import pl.noxhours.rate.RateService;
import pl.noxhours.timesheet.Timesheet;
import pl.noxhours.timesheet.TimesheetService;
import pl.noxhours.user.User;
import pl.noxhours.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final TimesheetService timesheetService;
    private final RateService rateService;
    private final UserService userService;

    public void create(Report report) {
        report.setCreated(LocalDateTime.now());
        if (report.getBasedOnClient() == null) {
            report.setBasedOnClient(false);
        }
        if (report.getBasedOnUser() == null) {
            report.setBasedOnUser(false);
        }
        if (report.getShowDetails() == null) {
            report.setShowDetails(false);
        }
        if (report.getShowNames() == null) {
            report.setShowNames(false);
        }
        if (report.getShowRates() == null) {
            report.setShowRates(false);
        }
        if (!report.getBasedOnUser()) {
            report.setBaseUser(null);
        }
        if (!report.getBasedOnClient()) {
            report.setBaseClient(null);
        }
        if (!report.getShowDetails() && report.getShowNames()) {
            report.setShowNames(false);
        }
        reportRepository.save(report);
    }

    public Report read(Long id) {
        return reportRepository.getById(id);
    }

    public void update(Report report) {
        reportRepository.save(report);
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }

    public Page<Report> findAll(Pageable pageable) {
        return reportRepository.findAllByCreator(pageable, userService.read(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public List<Report> findAll(User user) {
        return reportRepository.findAllByCreatorOrBaseUser(user, user);
    }

    public List<Report> findAll(Client client) {
        return reportRepository.findAllByBaseClient(client);
    }

    public void replaceUserWithDto(Report report) {

        report.setCreatorDTO(userService.userToUserNameDto(report.getCreator()));
        report.setCreator(null);
        if (report.getBaseUser() != null) {
            report.setBaseUserDTO(userService.userToUserNameDto(report.getBaseUser()));
            report.setBaseUser(null);
        } else {
            report.setBaseUserDTO(null);
        }
    }

    public void generate(Report report) {

        report.setHoursByRank(new ArrayList<>());

        List<Timesheet> result = new ArrayList<>();

        if (report.getBasedOnClient() && report.getBasedOnUser()) {
            result = timesheetService.findAll(report.getBaseUser(), report.getBaseClient(), report.getDateFrom(), report.getDateTo());
        } else {
            if (report.getBasedOnClient()) {
                result = timesheetService.findAll(report.getBaseClient(), report.getDateFrom(), report.getDateTo());
            }
            if (report.getBasedOnUser()) {
                result = timesheetService.findAll(report.getBaseUser(), report.getDateFrom(), report.getDateTo());
            }
            if (!report.getBasedOnClient() && !report.getBasedOnUser()) {
                result = timesheetService.findAll(report.getDateFrom(), report.getDateTo());
            }
        }
        if (result == null) {
            result = new ArrayList<>();
        }
        report.setTimesheets(result);
        for (int i = 1; i < 5; i++) {
            int tempRank = i;
            report.getHoursByRank().add(result.stream().filter(item -> item.getRankWhenCreated() == tempRank).map(Timesheet::getHours).reduce(0, Integer::sum));
        }
        report.setTotalHours(report.getHoursByRank().stream().reduce(0, Integer::sum));

        if (report.getShowRates() && rateService.validateRateAvailablity(result)) {
            rateService.getTimesheetValueForRank(report);
            report.setTotalValue(report.getValueByRank().stream().reduce(new BigDecimal(0), BigDecimal::add));
        }
//        report.setTimesheets(report.getTimesheets().stream().peek(timesheetService::replaceUserWithDto).collect(Collectors.toList()));
        report.getTimesheets().forEach(timesheetService::replaceUserWithDto);
        replaceUserWithDto(report);
    }
}
