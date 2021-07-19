package pl.noxhours.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.noxhours.client.Client;

import java.util.List;

@Repository
@Transactional
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findFirst10ByOrderByCreatedDesc();

    List<Activity> findAllByClient(Client client);
}
