package pl.noxhours.user;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.noxhours.client.Client;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<String, User> {

    private final UserService userService;

    @Override
    public User convert(String s) {
        if (!s.matches("\\d+")) {
            return null;
        }
        return userService.read(Long.parseLong(s));
    }
}
