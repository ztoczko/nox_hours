package pl.noxhours.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.GlobalConstants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = Activity.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    public static final String TABLE_NAME = "activities";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = GlobalConstants.DATE_TIME_FORMAT)
    private LocalDateTime created;

    private String fullName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public String getCreatedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT).format(created);
    }
}
