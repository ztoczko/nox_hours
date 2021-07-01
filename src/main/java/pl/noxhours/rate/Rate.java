package pl.noxhours.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.noxhours.configuration.GlobalConstants;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;

    @Column(name = "partner_rate")
    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
    private BigDecimal partnerRate;

    @Column(name = "attorney_rate")
    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
    private BigDecimal attorneyRate;

    @Column(name = "applicant_rate")
    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
    private BigDecimal applicantRate;

    @Column(name = "student_rate")
    @Pattern(regexp = GlobalConstants.CASH_VALUE_REGEXP)
    private BigDecimal studentRate;

}
