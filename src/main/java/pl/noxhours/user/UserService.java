package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.noxhours.configuration.EmailService;
import pl.noxhours.report.ReportService;
import pl.noxhours.timesheet.TimesheetService;
import pl.noxhours.user.DTO.*;

import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final MessageSource messageSource;

    private TimesheetService timesheetService;
    private ReportService reportService;

    @Autowired
    public void setTimesheetService(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    public void create(User user) {
        userRepository.save(user);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created user with id of " + user.getId());
    }

    public User read(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User read(String email) {
        return userRepository.getFirstByEmail(email);
    }

    public void update(User user) {
        userRepository.save(user);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated user with id of " + user.getId());
    }

    public void update(User user, boolean logFlag) {
        userRepository.save(user);
        if (logFlag) {
            log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated user with id of " + user.getId());
        }
    }

    public void delete(User user) {
        timesheetService.findAll(user).forEach(timesheetService::delete);
        reportService.findAll(user).forEach(reportService::delete);
        userRepository.delete(user);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " deleted user with id of " + user.getId());
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

    public boolean checkResetKey(String resetKey) {
        return userRepository.getFirstByPasswordResetKey(resetKey) != null;
    }

    public boolean checkEditPermissionForAdmin(User user) {

        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN")) || (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) && !user.getPrivileges().contains("S"));
    }

    public void setPasswordResetKey(User user) {

        String resetKey = generateRandomKey();
        user.setPasswordResetKey(resetKey);
        update(user, false);
        emailService.sendMessage(user.getEmail(), messageSource.getMessage("email.password.reset.subject", null, LocaleContextHolder.getLocale()), messageSource.getMessage("email.password.reset.body", new String[]{(ServletUriComponentsBuilder.fromCurrentContextPath().replacePath(null).build().toUriString() + "/reset/" + URLEncoder.encode(resetKey))}, LocaleContextHolder.getLocale()), null);
    }

    public void setPasswordResetKeyForNewUser(User user) {

        String resetKey = generateRandomKey();
        user.setPasswordResetKey(resetKey);
        emailService.sendMessage(user.getEmail(), messageSource.getMessage("email.password.new.subject", null, LocaleContextHolder.getLocale()), messageSource.getMessage("email.password.new.body", new String[]{(ServletUriComponentsBuilder.fromCurrentContextPath().replacePath(null).build().toUriString() + "/reset/" + URLEncoder.encode(resetKey))}, LocaleContextHolder.getLocale()), null);
    }

    public UserNameDTO userToUserNameDto(User user) {
        return new UserNameDTO(user.getId(), user.getFullName());
    }

    public User userNameDtoToUser(UserNameDTO userNameDTO) {
        return read(userNameDTO.getId());
    }

    public UserSettingsDTO userToUserSettingsDto(User user) {
        return new UserSettingsDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public User userSettingsDtoToUser(UserSettingsDTO userSettingsDTO) {
        User user = read(userSettingsDTO.getId());
        user.setFirstName(userSettingsDTO.getFirstName());
        user.setLastName(userSettingsDTO.getLastName());
        user.setEmail(userSettingsDTO.getEmail());
        return user;
    }

    public UserPasswordResetDTO userToUserPasswordResetDto(User user) {
        return new UserPasswordResetDTO(user.getId());
    }

    public UserPasswordChangeDTO userToUserPasswordChangeDto(User user) {
        return new UserPasswordChangeDTO(user.getId());
    }

    public User userPasswordDtoToUser(UserPasswordResetDTO userPasswordResetDTO) {
        User user = read(userPasswordResetDTO.getId());
        user.setPassword(new BCryptPasswordEncoder(10).encode(userPasswordResetDTO.getNewPassword()));
        return user;
    }

    public UserAdminListDTO userToUserAdminListDto(User user) {
        return new UserAdminListDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRank(), privilegesStringToArray(user.getPrivileges()), user.getIsLocked());
    }

    public User userAdminListDtoToUser(UserAdminListDTO userAdminListDTO) {
        User user = userAdminListDTO.getId() == null ? new User() : read(userAdminListDTO.getId());
        user.setFirstName(userAdminListDTO.getFirstName());
        user.setLastName(userAdminListDTO.getLastName());
        user.setEmail(userAdminListDTO.getEmail());
        user.setRank(userAdminListDTO.getRank());
        user.setPrivileges(privilegesArrayToString(userAdminListDTO.getPrivileges()));
        user.setIsLocked(userAdminListDTO.getIsLocked() != null && userAdminListDTO.getIsLocked());
        return user;
    }

    private String generateRandomKey() {

        Random rand = new Random();
        String result = new String();
        for (int i = 1; i < 21; i++) {
            result += (char) (48 + rand.nextInt(75));
        }
        return result;
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
