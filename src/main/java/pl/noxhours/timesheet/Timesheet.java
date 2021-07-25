package pl.noxhours.timesheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import pl.noxhours.case_.Case;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.customValidation.CheckTimesheetTime;
import pl.noxhours.user.DTO.UserNameDTO;
import pl.noxhours.user.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = Timesheet.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
@CheckTimesheetTime
public class Timesheet {
    public static final String TABLE_NAME = "timesheets";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = GlobalConstants.DATE_TIME_FORMAT)
    private LocalDateTime created;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(pattern = GlobalConstants.DATE_FORMAT)
    @Column(name = "date_executed")
    private LocalDate dateExecuted;

    @NotNull
    @PositiveOrZero
    private Integer hours;

    @NotNull
    @Range(min = 0, max = 59)
    private Integer minutes;

    @NotNull
    @Size(max = 255)
    private String description;

    @Column(name = "rank_when_created")
    @Range(min = 1, max = 4)
    private Byte rankWhenCreated;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case clientCase;

    @Transient
    private UserNameDTO userNameDTO;

    public String getCreatedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT).format(created);
    }

    public String getDateExecutedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT).format(dateExecuted);
    }

    public String getShortDescription() {
        return description == null || description.length() < 18 ? description : description.substring(0, 15).concat("...");
    }

    public String getHoursString() {
        return hours + "h" + (minutes < 10 ? "0" : "") + minutes + "min";
    }

}
