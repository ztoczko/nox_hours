package pl.noxhours.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.noxhours.configuration.GlobalConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = Client.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    public static final String TABLE_NAME = "clients";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    @Size(min = 3, max = 255)
    @NotNull
    private String name;

    private Boolean closed;

    public String getCreatedString() {
        return DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT).format(created);
    }
}
