package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import pl.noxhours.timesheet.TimesheetService;
import pl.noxhours.user.DTO.*;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private TimesheetService timesheetService;

    @Autowired
    public void setTimesheetService(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

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
        timesheetService.findAll(user).forEach(timesheetService::delete);
        userRepository.delete(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> findAllActive() {
        return userRepository.findAllByIsLocked(false);
    }

    public Page<User> findAll(Pageable pageable, Boolean all) {
        return all ? userRepository.findAll(pageable) : userRepository.findAllByIsLocked(pageable, false);
    }

    public Page<User> findAll(Pageable pageable, Boolean all, String search) {
        return all ? userRepository.findAll(pageable, search) : userRepository.findAllByIsLocked(pageable, false, search);
    }

    public boolean checkEditPermissionForAdmin(User user) {

        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN")) || (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) && !user.getPrivileges().contains("S"));
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

    public UserAdminListDTO UserToUserAdminListDto(User user) {
        return new UserAdminListDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRank(), privilegesStringToArray(user.getPrivileges()), user.getIsLocked());
    }

    public User UserAdminListDtoToUser(UserAdminListDTO userAdminListDTO) {
        User user = userAdminListDTO.getId() == null ? new User() : read(userAdminListDTO.getId());
        user.setFirstName(userAdminListDTO.getFirstName());
        user.setLastName(userAdminListDTO.getLastName());
        user.setEmail(userAdminListDTO.getEmail());
        user.setRank(userAdminListDTO.getRank());
        user.setPrivileges(privilegesArrayToString(userAdminListDTO.getPrivileges()));
        user.setIsLocked(userAdminListDTO.getIsLocked() != null && userAdminListDTO.getIsLocked());
        return user;
    }

    private String[] privilegesStringToArray(String privileges) {

        if (privileges.isEmpty()) {
            return new String[]{"U"};
        }
        String[] result = new String[privileges.length() + 1];
        result[0] = "U";
        for (int i = 0; i < privileges.length(); i++) {
            result[i + 1] = privileges.substring(i, i + 1);
        }
        return result;
    }

    private String privilegesArrayToString(String[] privileges) {

        if (privileges == null || privileges.length == 0) {
            return "";
        }
        String result = new String();

        for (String str : privileges) {
            if (!str.equals("U") && !result.contains(str)) {
                result += str;
            }
        }
        return result;
    }

}
