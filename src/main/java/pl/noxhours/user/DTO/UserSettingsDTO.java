package pl.noxhours.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.customValidation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UniqueEmail
public class UserSettingsDTO extends AbstractUserEmail {

    private Long id;

    @Pattern(regexp = GlobalConstants.FIRST_NAME_REGEXP, message = "{javax.validation.constraints.Pattern.firstName.message}")
    private String firstName;

    @Pattern(regexp = GlobalConstants.LAST_NAME_REGEXP, message = "{javax.validation.constraints.Pattern.lastName.message}")
    @Size(max = 255)
    private String lastName;

    @Email
    @Size(max = 255)
    @NotBlank
    private String email;


}
