package pl.noxhours.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.noxhours.rate.RateService;
import pl.noxhours.timesheet.Timesheet;
import pl.noxhours.timesheet.TimesheetService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TimesheetService timesheetService;
    private final RateService rateService;

    public void generate(Report report) {

        report.setHoursByRank(new ArrayList<>());

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
        report.setTimesheets(result);
        for (int i = 1; i < 5; i++) {
            int tempRank = i;
            report.getHoursByRank().add(result.stream().filter(item -> item.getRankWhenCreated() == tempRank).map(Timesheet::getHours).reduce(0, Integer::sum));
        }
        report.setTotalHours(report.getHoursByRank().stream().reduce(0, Integer::sum));

        if (report.getShowRates()) {
            rateService.getTimesheetValueForRank(report);
            report.setTotalValue(report.getValueByRank().stream().reduce(new BigDecimal(0), BigDecimal::add));
        }

    }
}
