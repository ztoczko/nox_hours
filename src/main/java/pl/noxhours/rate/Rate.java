package pl.noxhours.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = Rate.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rate {

    public static final String TABLE_NAME = "rates";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_from")
    @DateTimeFormat(pattern = GlobalConstants.DATE_FORMAT)
    private LocalDate dateFrom;

    @Column(name = "date_to")
    @DateTimeFormat(pattern = GlobalConstants.DATE_FORMAT)
    private LocalDate dateTo;

    @Column(name = "partner_rate")
//    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
//    @NotEmpty
    private BigDecimal partnerRate;

    @Column(name = "attorney_rate")
//    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
//    @NotEmpty
    private BigDecimal attorneyRate;

    @Column(name = "applicant_rate")
//    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
//    @NotEmpty
    private BigDecimal applicantRate;

    @Column(name = "student_rate")
//    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
//    @NotEmpty
    private BigDecimal studentRate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull
    private Client client;

    @Transient
    private Boolean rateNotExpires;

    public String getDateFromString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT).format(dateFrom);
    }

    public String getDateToString() {
        if (dateTo.isAfter(LocalDate.now().plusYears(100))) {
            return "";
        }
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT).format(dateTo);
    }

    public Rate clone() {
        return new Rate(null, dateFrom, dateTo, partnerRate, attorneyRate, applicantRate, studentRate, client, rateNotExpires);
    }

    public String toString() {
        return "some rate";
    }

}
