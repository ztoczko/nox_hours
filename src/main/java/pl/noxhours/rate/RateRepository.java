package pl.noxhours.rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.noxhours.client.Client;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface RateRepository extends JpaRepository<Rate, Long> {

    Page<Rate> findAllByClient(Pageable pageable, Client client);

    List<Rate> findAllByClientOrderByDateToDesc(Client client);

    Rate findFirstByClientAndDateFromLessThanEqualAndDateToGreaterThanEqual(Client client, LocalDate date, LocalDate date1);
}
