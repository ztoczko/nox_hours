package pl.noxhours.rate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;

    public void create(Rate rate) {
        rateRepository.save(rate);
    }

    public Rate read(Long id) {
        return rateRepository.findById(id).orElse(null);
    }

    public void update(Rate rate) {
        rateRepository.save(rate);
    }

    public void delete(Rate rate) {
        rateRepository.delete(rate);
    }

    public List<Rate> getAll() {
        return rateRepository.findAll();
    }
}
