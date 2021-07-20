package pl.noxhours.report;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.EmailService;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.rate.RateService;
import pl.noxhours.timesheet.Timesheet;
import pl.noxhours.timesheet.TimesheetService;
import pl.noxhours.user.User;
import pl.noxhours.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final TimesheetService timesheetService;
    private final RateService rateService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final EmailService emailService;

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
        return reportRepository.findById(id).orElse(null);
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

    public boolean sendMail(Report report) {
       return emailService.sendMessage(SecurityContextHolder.getContext().getAuthentication().getName(), "test", generateHtmlMessage(report, new Locale("pl", "PL")));
    }

    public String generateHtmlMessage(Report report, Locale locale) {
        generate(report);

        String message = GlobalConstants.EMAIL_HEAD;
        message += "\n<p>" + messageSource.getMessage("email.report.head", null, locale) + " " + report.getCreatedString() + "</p>\n";
        message += "<table style=\"border: thin solid black; border-collapse: collapse;\">\n<thead>\n<tr>\n<th colspan=\"4\">\n" + messageSource.getMessage("report.show.aggregate", null, locale) + "\n<br>\n" + messageSource.getMessage("report.show.aggregate.msg", new String[]{report.getDateFromString(), report.getDateToString()}, locale) + "\n</th>\n</tr>\n</thead>\n";
        message += "<tbody>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRank().get(0) + "h\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRank().get(1) + "h\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRank().get(2) + "h\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRank().get(3) + "h\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right; font-weight: bold;\">\n" + messageSource.getMessage("report.show.total.hours", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center; font-weight: bold;\">\n" + report.getTotalHours() + "h\n</td>\n</tr>\n";
        if (report.getShowRates() && SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            if (report.getTotalValue() == null) {
                message += "<tr>\n<td colspan=\"4\">\n" + messageSource.getMessage("report.show.rates.error", null, locale) + "\n</td>\n</tr>\n";
            } else {
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(0) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(1) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(2) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(3) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right; font-weight: bold;\">\n" + messageSource.getMessage("report.show.total.value", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center; font-weight: bold;\">\n" + report.getTotalValue() + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
            }
        }
        message += "</tbody></table>";


        if (report.getShowDetails()) {
            message += "\n<br><br>\n";
            message += "<table style=\"border: thin solid black; border-collapse: collapse;\">\n<thead>\n<tr>\n<th colspan=\"" + (report.getShowNames() ? "6" : "5") + "\">\n" + messageSource.getMessage("report.show.details", null, locale) + "\n</th>\n</tr>\n</thead>\n";
            message += "<tbody>\n<tr>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.date.executed", null, locale) + "\n</th>\n";
            if (report.getShowNames()) {
                message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.user", null, locale) + "\n</th>\n";
            }
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.rank.when.created", null, locale) + "\n</th>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.client", null, locale) + "\n</th>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.hours", null, locale) + "\n</th>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.description", null, locale) + "\n</th>\n";
            message += "</tr>\n";
            if (report.getTimesheets().size() == 0) {
                message += "<tr>\n<td colspan=\"" + (report.getShowNames() ? "6" : "5") + "\" style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("report.show.no.timesheets.message", null, locale) + "\n</td>\n</tr>\n";
            }
            for (Timesheet timesheet : report.getTimesheets()) {
                message += "<tr>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getDateExecutedString() + "\n</td>\n";
                if (report.getShowNames()) {
                    message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getUserNameDTO().getFullName() + "\n</td>\n";
                }
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n";
                switch (timesheet.getRankWhenCreated()) {
                    case 1:
                        message += messageSource.getMessage("user.rank.student", null, locale);
                        break;
                    case 2:
                        message += messageSource.getMessage("user.rank.applicant", null, locale);
                        break;
                    case 3:
                        message += messageSource.getMessage("user.rank.attorney", null, locale);
                        break;
                    case 4:
                        message += messageSource.getMessage("user.rank.partner", null, locale);
                        break;
                }
                message += "\n</td>\n";
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getClient().getName() + "\n</td>\n";
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getHours() + "\n</td>\n";
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getDescription() + "\n</td>\n</tr>\n";
            }
            message += "</tbody></table>\n";
        }

        message += "<br><br><p>" + messageSource.getMessage("email.footer", null, locale) + "</p>";

        return message;
    }
}
