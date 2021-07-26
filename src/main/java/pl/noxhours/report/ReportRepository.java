package pl.noxhours.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.noxhours.case_.Case;
import pl.noxhours.client.Client;
import pl.noxhours.user.User;

import java.util.List;

@Repository
@Transactional
public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findAllByCreator(Pageable pageable, User user);

    List<Report> findAllByCreatorOrBaseUser(User user, User user1);

    List<Report> findAllByBaseClient(Client client);

    List<Report> findAllByBaseCase(Case aCase);
}
