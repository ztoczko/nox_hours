package pl.noxhours.case_;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaseConverter implements Converter<String, Case> {

    private final CaseService caseService;

    @Override
    public Case convert(String s) {
        if (!s.matches("\\d+")) {
            return null;
        }
        return caseService.read(Long.parseLong(s));
    }
}
