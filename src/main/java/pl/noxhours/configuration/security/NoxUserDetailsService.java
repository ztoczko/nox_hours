package pl.noxhours.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.noxhours.user.User;
import pl.noxhours.user.UserService;

@Service
@RequiredArgsConstructor
public class NoxUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.read(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new NoxUserDetails(user);
    }
}
