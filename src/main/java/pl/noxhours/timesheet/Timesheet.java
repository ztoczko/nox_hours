package pl.noxhours.timesheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = Timesheet.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @Positive
    private Integer hours;

    @NotNull
    @Size(max = 255)
    private String description;

    @Column(name = "rank_when_created")
    @Range(min = 1, max = 4)
    private Integer rankWhenCreated;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public String getCreatedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT).format(created);
    }

    public String getDateExecutedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT).format(dateExecuted);
    }
}
