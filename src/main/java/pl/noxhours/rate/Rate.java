package pl.noxhours.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.customValidation.CashValue;
import pl.noxhours.customValidation.RateDateOrder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = Rate.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
@RateDateOrder
public class Rate {

    public static final String TABLE_NAME = "rates";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_from")
    @DateTimeFormat(pattern = GlobalConstants.DATE_FORMAT)
    @NotNull
    private LocalDate dateFrom;

    @Column(name = "date_to")
    @DateTimeFormat(pattern = GlobalConstants.DATE_FORMAT)
    private LocalDate dateTo;

    @Column(name = "partner_rate")
    @CashValue
    private BigDecimal partnerRate;

    @Column(name = "attorney_rate")
    @CashValue
    private BigDecimal attorneyRate;

    @Column(name = "applicant_rate")
    @CashValue
    private BigDecimal applicantRate;

    @Column(name = "student_rate")
    @CashValue
    private BigDecimal studentRate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Transient
    private Boolean rateNotExpires;

    public Rate(Boolean rateNotExpires, Client client) {
        this.rateNotExpires = rateNotExpires;
        this.client = client;
    }

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
