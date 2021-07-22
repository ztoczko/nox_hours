package pl.noxhours.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.noxhours.customValidation.CheckPassword;
import pl.noxhours.customValidation.CheckResetKey;
import pl.noxhours.customValidation.NewPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NewPassword
@CheckResetKey
public class UserPasswordResetMailDTO extends UserPasswordResetDTO{

    private String email;

    private String resetKey;

    public UserPasswordResetMailDTO(Long id) {
        super(id);
    }

    public UserPasswordResetMailDTO(String resetKey) {
        super();
        this.resetKey = resetKey;
    }

}
