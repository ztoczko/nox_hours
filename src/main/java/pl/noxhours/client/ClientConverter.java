package pl.noxhours.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientConverter implements Converter<String, Client> {

    private final ClientService clientService;

    @Override
    public Client convert(String s) {
        if (!s.matches("\\d+")) {
            return null;
        }
        return clientService.read(Long.parseLong(s));
    }
}
