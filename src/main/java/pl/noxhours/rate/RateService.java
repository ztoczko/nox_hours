package pl.noxhours.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;
import pl.noxhours.report.Report;
import pl.noxhours.timesheet.Timesheet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;

    public void create(Rate rate) {
        if (rate.getRateNotExpires() != null && rate.getRateNotExpires()) {
            rate.setDateTo(LocalDate.now().plusYears(200));
        }
        solveCollision(rate);
        createWithoutCollisionCheck(rate);
    }

    private void createWithoutCollisionCheck(Rate rate) {
        rateRepository.save(rate);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created new rate with id of " + rate.getId() + " for client with id of " + rate.getClient().getId());
    }

    public Rate read(Long id) {
        return rateRepository.findById(id).orElse(null);
    }

    public void update(Rate rate) {
        rateRepository.save(rate);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " updated rate with id of " + rate.getId() + " for client with id of " + rate.getClient().getId());
    }

    public void delete(Rate rate) {
        rateRepository.delete(rate);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " deleted rate with id of " + rate.getId() + " from client with id of " + rate.getClient().getId());
    }

    public List<Rate> findAll() {
        return rateRepository.findAll();
    }

    public Page<Rate> findAllByClient(Pageable pageable, Client client) {
        return rateRepository.findAllByClient(pageable, client);
    }

    public List<Rate> findAllByClient(Client client) {
        return rateRepository.findAllByClientOrderByDateToDesc(client);
    }

    public void getTimesheetValueForRank(Report report) {

        List<BigDecimal> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            result.add(new BigDecimal(0));
        }
        Rate tempRate;
        for (Timesheet timesheet : report.getTimesheets()) {
            tempRate = getRateForDate(timesheet.getClient(), timesheet.getDateExecuted());
            switch (timesheet.getRankWhenCreated()) {
                case 1:
                    result.set(0, result.get(0).add(tempRate.getStudentRate().multiply(BigDecimal.valueOf(timesheet.getHours()).add(BigDecimal.valueOf(timesheet.getMinutes()).divide(BigDecimal.valueOf(60))))));
                    break;
                case 2:
                    result.set(1, result.get(1).add(tempRate.getApplicantRate().multiply(BigDecimal.valueOf(timesheet.getHours()).add(BigDecimal.valueOf(timesheet.getMinutes()).divide(BigDecimal.valueOf(60))))));
                    break;
                case 3:
                    result.set(2, result.get(2).add(tempRate.getAttorneyRate().multiply(BigDecimal.valueOf(timesheet.getHours()).add(BigDecimal.valueOf(timesheet.getMinutes()).divide(BigDecimal.valueOf(60))))));
                    break;
                case 4:
                    result.set(3, result.get(3).add(tempRate.getPartnerRate().multiply(BigDecimal.valueOf(timesheet.getHours()).add(BigDecimal.valueOf(timesheet.getMinutes()).divide(BigDecimal.valueOf(60))))));
                    break;
            }
        }
        for (int i = 0; i < 4; i++) {
            result.set(i, result.get(i).setScale(2, RoundingMode.HALF_UP));
        }
        report.setValueByRank(result);

    }

    public boolean validateRateAvailablity(List<Timesheet> timesheets) {

        boolean isValid = true;
        for (Timesheet timesheet : timesheets) {
            if (getRateForDate(timesheet.getClient(), timesheet.getDateExecuted()) == null) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    private Rate getRateForDate(Client client, LocalDate date) {
        return rateRepository.findFirstByClientAndDateFromLessThanEqualAndDateToGreaterThanEqual(client, date, date);
    }

    private void solveCollision(Rate rate) {
        List<Rate> rates = rateRepository.findAllByClientOrderByDateToDesc(rate.getClient());
        Rate tempRate;
        for (Rate pastRate : rates) {
            if (pastRate.getId().equals(rate.getId())) { //Test in case of possibility to edit rates will be implemented in future
                continue;
            }
            tempRate = pastRate.clone();
            //old rate is consumed in full by new rate
            if (!rate.getDateFrom().isAfter(pastRate.getDateFrom()) && !rate.getDateTo().isBefore(pastRate.getDateTo())) {
                delete(pastRate);
            } else {
                //new rate start date is in old rate duration
                if (!rate.getDateFrom().isBefore(pastRate.getDateFrom()) && !rate.getDateFrom().isAfter(pastRate.getDateTo())) {
                    pastRate.setDateTo(rate.getDateFrom().minusDays(1));
                    update(pastRate);
                }
                //new rate end date is in old rate duration
                if (!rate.getDateTo().isBefore(pastRate.getDateFrom()) && !rate.getDateTo().isAfter(pastRate.getDateTo())) {
                    pastRate.setDateFrom(rate.getDateTo().plusDays(1));
                    update(pastRate);
                }
                //new rate is "in the middle of old rate" assuming it's not last rate
                if (rate.getDateTo().isBefore(tempRate.getDateTo()) && rate.getDateFrom().isAfter(tempRate.getDateFrom()) && !tempRate.getDateTo().isAfter(LocalDate.now())) {
                    tempRate.setDateFrom(rate.getDateTo().plusDays(1));
                    createWithoutCollisionCheck(tempRate);
                }
            }
        }
    }
}
