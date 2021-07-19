package pl.noxhours.report;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportConverter implements Converter<String, Report> {

    private final ReportService reportService;

    @Override
    public Report convert(String s) {
        if (!s.matches("\\d+")) {
            return null;
        }
        return reportService.read(Long.parseLong(s));
    }
}
