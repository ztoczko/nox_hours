package pl.noxhours.rate;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateConverter implements Converter<String, Rate> {

    private final RateService rateService;

    @Override
    public Rate convert(String s) {
        if (!s.matches("\\d+")) {
            return null;
        }
        return rateService.read(Long.parseLong(s));
    }
}
