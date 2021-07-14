package pl.noxhours.timesheet;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimesheetConverter implements Converter<String, Timesheet> {

    private final TimesheetService timesheetService;

    @Override
    public Timesheet convert(String s) {
        if (!s.matches("\\d+")) {
            return null;
        }
        return timesheetService.read(Long.parseLong(s));
    }
}
