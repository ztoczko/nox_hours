package pl.noxhours.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;

    public void create(Rate rate) {
//        Rate oldRate = rateRepository.findFirstByClientOrderByDateToDesc(rate.getClient());
//        oldRate.setDateTo(rate.getDateFrom().minusDays(1));
//        update(oldRate);
        if (rate.getRateNotExpires() != null && rate.getRateNotExpires()) {
            rate.setDateTo(LocalDate.now().plusYears(200));
        }
        solveCollision(rate);
        createWithoutCollisionCheck(rate);
    }

    private void createWithoutCollisionCheck(Rate rate) {
        rateRepository.save(rate);
        log.info("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " created new rate with od of " + rate.getId() + " for client with id of " + rate.getClient().getId());
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
