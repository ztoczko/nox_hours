package pl.noxhours.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.noxhours.customValidation.NewPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NewPassword
public class UserPasswordResetDTO {

    private Long id;

    private String newPassword;

    private String newPasswordRepeated;

    public UserPasswordResetDTO(Long id) {
        this.id = id;
    }
}
