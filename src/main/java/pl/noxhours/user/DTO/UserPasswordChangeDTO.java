package pl.noxhours.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.noxhours.customValidation.CheckPassword;
import pl.noxhours.customValidation.NewPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NewPassword
@CheckPassword
public class UserPasswordChangeDTO extends UserPasswordResetDTO{

    private String oldPassword;

    public UserPasswordChangeDTO(Long id) {
        super(id);
    }

}
