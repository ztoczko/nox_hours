package pl.noxhours.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findAllBy(Pageable pageable);

    Page<Client> findAllByClosed(Pageable pageable, Boolean closed);

    Page<Client> findAllByNameContains(Pageable pageable, String search);

    Page<Client> findAllByClosedAndNameContains(Pageable pageable, Boolean closed, String search);
}
