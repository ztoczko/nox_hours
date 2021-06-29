package pl.noxhours.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.noxhours.user.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User getFirstByEmail(String email);

}
