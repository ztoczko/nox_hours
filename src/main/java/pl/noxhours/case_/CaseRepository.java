package pl.noxhours.case_;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.noxhours.client.Client;

import java.util.List;

@Repository
@Transactional
public interface CaseRepository extends JpaRepository<Case, Long> {

    List<Case> findAllByClientOrderByCreatedDesc(Client client);

    Page<Case> findAllByClient(Pageable pageable, Client client);

    List<Case> findAllByClientAndClosedOrderByCreatedDesc(Client client, Boolean closed);

    Page<Case> findAllByClientAndClosed(Pageable pageable, Client client, Boolean closed);

    List<Case> findAllByClosed(Boolean closed);
}
