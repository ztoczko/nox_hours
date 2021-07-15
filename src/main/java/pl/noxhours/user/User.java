package pl.noxhours.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.customValidation.UniqueEmail;
import pl.noxhours.user.DTO.AbstractUserEmail;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = User.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User extends AbstractUserEmail {

    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Pattern(regexp = GlobalConstants.FIRST_NAME_REGEXP, message = "{javax.validation.constraints.Pattern.firstName.message}")
//    @Size(min = 10)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Pattern(regexp = GlobalConstants.LAST_NAME_REGEXP, message = "{javax.validation.constraints.Pattern.lastName.message}")
    @Size(max = 255)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email
    @Size(max = 255)
    private String email;

    @Column(nullable = false, length = 60)
    @Size(max = 60)
    private String password;

    @Column(name = "password_reset", nullable = false /**columnDefinition = "BYTE"*/)
    private Boolean passwordReset;

    @Column(name = "password_reset_key", length = 10)
    private String passwordResetKey;

    @Column(name = "user_rank", nullable = false)
    private Byte rank;

    @Column(length = 4)
    @Size(max = 4)
//    Basic/Rates/Admin/Superadmin
    private String privileges;

    @Column(name = "locked", nullable = false)
    private Boolean isLocked;

    @Column(name = "deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_date")
    private LocalDateTime deleteDate;

    public User(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
