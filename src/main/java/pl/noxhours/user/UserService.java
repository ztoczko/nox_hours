package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.noxhours.user.DTO.UserNameDTO;

import java.util.List;

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

}
