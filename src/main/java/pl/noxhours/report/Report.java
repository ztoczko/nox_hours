package pl.noxhours.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.customValidation.ReportCheckClient;
import pl.noxhours.customValidation.ReportCheckUser;
import pl.noxhours.customValidation.ReportDateOrder;
import pl.noxhours.timesheet.Timesheet;
import pl.noxhours.user.DTO.UserNameDTO;
import pl.noxhours.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = Report.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ReportDateOrder
@ReportCheckClient
@ReportCheckUser
public class Report {

    public static final String TABLE_NAME = "reports";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = GlobalConstants.DATE_TIME_FORMAT)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @NotNull
    private User creator;

    @Column(name = "date_from")
    @NotNull
    @DateTimeFormat(pattern = GlobalConstants.DATE_FORMAT)
    private LocalDate dateFrom;

    @Column(name = "date_to")
    @NotNull
    @DateTimeFormat(pattern = GlobalConstants.DATE_FORMAT)
    private LocalDate dateTo;

    @Column(name = "based_on_client")
    private Boolean basedOnClient;

    @Column(name = "based_on_user")
    private Boolean basedOnUser;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client baseClient;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User baseUser;

    @Column(name = "show_details")
    private Boolean showDetails;

    @Column(name = "show_names")
    private Boolean showNames;

    @Column(name = "show_rates")
    private Boolean showRates;

    @Transient
    private List<Timesheet> timesheets;

    @Transient
    private List<Integer> hoursByRank;

    @Transient
    private Integer totalHours;

    @Transient
    private List<BigDecimal> valueByRank;

    @Transient
    private BigDecimal totalValue;

    @Transient
    private UserNameDTO creatorDTO;

    @Transient
    private UserNameDTO baseUserDTO;

    public String getCreatedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT).format(created);
    }

    public String getDateFromString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT).format(dateFrom);
    }

    public String getDateToString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT).format(dateTo);
    }

}
