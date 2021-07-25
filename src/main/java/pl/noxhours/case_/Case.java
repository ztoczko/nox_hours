package pl.noxhours.case_;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = Case.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Case {

    public static final String TABLE_NAME = "cases";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = GlobalConstants.DATE_TIME_FORMAT)
    private LocalDateTime created;

    @NotNull
    @Size(min=3, max = 255)
    private String name;

    @NotNull
    private Boolean closed;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Case(Client client) {
        this.client = client;
    }

    public String getCreatedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT).format(created);
    }

    public String getShortName() {
        return name.length() < 18 ? name : name.substring(0, 15).concat("...");
    }

}
