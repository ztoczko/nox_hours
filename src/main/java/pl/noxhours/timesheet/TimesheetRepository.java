package pl.noxhours.timesheet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.noxhours.client.Client;
import pl.noxhours.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    List<Timesheet> findAllByDateExecutedBetween(LocalDate dateFrom, LocalDate dateTo);

    Page<Timesheet> findAllByClient(Pageable pageable, Client client);

    List<Timesheet> findAllByClient(Client client);

    List<Timesheet> findAllByClientAndDateExecutedBetween(Client client, LocalDate dateFrom, LocalDate dateTo);

    Page<Timesheet> findAllByUser(Pageable pageable, User user);

    List<Timesheet> findAllByUser(User user);

    List<Timesheet> findAllByUserAndDateExecutedBetween(User user, LocalDate dateFrom, LocalDate dateTo);

    List<Timesheet> findAllByUserAndClient(User user, Client client);

    List<Timesheet> findAllByUserAndClientAndDateExecutedBetween(User user, Client client, LocalDate dateFrom, LocalDate dateTo);

    @Query("SELECT sum(timesheet.hours) FROM Timesheet timesheet WHERE timesheet.user = :user AND timesheet.created > :dateFrom")
    Integer getSumOfRecent(User user, LocalDateTime dateFrom);

    Integer countByUserAndCreatedAfter(User user, LocalDateTime dateFrom);
}
