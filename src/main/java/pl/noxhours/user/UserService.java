package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import pl.noxhours.user.DTO.UserNameDTO;
import pl.noxhours.user.DTO.UserPasswordChangeDTO;
import pl.noxhours.user.DTO.UserPasswordResetDTO;
import pl.noxhours.user.DTO.UserSettingsDTO;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void create(User user) {
        userRepository.save(user);
    }

    public User read(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User read(String email) {
        return userRepository.getFirstByEmail(email);
    }

    public void update(User user) {
        userRepository.save(user);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated data of with id of " + user.getId());
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllActive() {
        return userRepository.findAllByIsDeleted(false);
    }

    public UserNameDTO UserToUserNameDto(User user) {
        return new UserNameDTO(user.getId(), user.getFullName());
    }

    public User UserNameDtoToUser(UserNameDTO userNameDTO) {
        return read(userNameDTO.getId());
    }

    public UserSettingsDTO UserToUserSettingsDto(User user) {
        return new UserSettingsDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public User UserSettingsDtoToUser(UserSettingsDTO userSettingsDTO) {
        User user = read(userSettingsDTO.getId());
        user.setFirstName(userSettingsDTO.getFirstName());
        user.setLastName(userSettingsDTO.getLastName());
        user.setEmail(userSettingsDTO.getEmail());
        return user;
    }

    public UserPasswordResetDTO UserToUserPasswordResetDto(User user) {
        return new UserPasswordResetDTO(user.getId());
    }

    public UserPasswordChangeDTO UserToUserPasswordChangeDto(User user) {
        return new UserPasswordChangeDTO(user.getId());
    }

    public User UserPasswordDtoToUser(UserPasswordResetDTO userPasswordResetDTO) {
        User user = read(userPasswordResetDTO.getId());
        user.setPassword(new BCryptPasswordEncoder(10).encode(userPasswordResetDTO.getNewPassword()));
        return user;
    }

}
