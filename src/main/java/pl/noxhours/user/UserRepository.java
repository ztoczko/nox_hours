package pl.noxhours.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User getFirstByEmail(String email);

    List<User> findAllByIsLocked(Boolean isLocked);

    Page<User> findAllByIsLocked(Pageable pageable, Boolean isLocked);

    @Query("SELECT user FROM User user WHERE user.isLocked = :isLocked AND user.firstName LIKE CONCAT('%', :search, '%') OR user.isLocked = :isLocked AND user.lastName LIKE CONCAT('%', :search, '%') OR user.isLocked = :isLocked AND user.email LIKE CONCAT('%', :search, '%')")
    Page<User> findAllByIsLocked(Pageable pageable, Boolean isLocked, String search);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT user FROM User user WHERE user.firstName LIKE CONCAT('%', :search, '%') OR user.lastName LIKE CONCAT('%', :search, '%') OR user.email LIKE CONCAT('%', :search, '%')")
    Page<User> findAll(Pageable pageable, String search);

    User getFirstByPasswordResetKey(String key);


}
