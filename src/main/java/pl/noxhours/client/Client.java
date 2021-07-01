package pl.noxhours.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    @Size(min = 3, max = 255)
    private String name;

    private Boolean closed;

    @Column(name = "rates_set")
    private Boolean ratesSet;

}
