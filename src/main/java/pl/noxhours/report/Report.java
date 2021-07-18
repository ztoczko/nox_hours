package pl.noxhours.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.noxhours.client.Client;
import pl.noxhours.timesheet.Timesheet;
import pl.noxhours.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Boolean basedOnClient;

    private Boolean basedOnUser;

    private Client baseClient;

    private User baseUser;

    private Boolean showDetails;

    private Boolean showNames;

    private Boolean showRates;

    private List<Timesheet> timesheets;

    private List<Integer> hoursByRank;

    private Integer totalHours;

    private List<BigDecimal> valueByRank;

    private BigDecimal totalValue;

}
