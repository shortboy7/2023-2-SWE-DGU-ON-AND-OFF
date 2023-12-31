package backend.spectrum.dguonoff.domain.reservation.repository;

import backend.spectrum.dguonoff.dao.Event;
import backend.spectrum.dguonoff.dao.Reservation;
import backend.spectrum.dguonoff.dao.model.ReservationStatus;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r where r.facility.code = ?1 and r.date > ?2 and r.status = ?3 order by r.date asc, r.startTime asc")
    Optional<List<Reservation>> findReservationsAfterYesterday(String code, LocalDate date, ReservationStatus status);
    @Query("select r.event from Reservation r where r.reservationId = ?1")
    Optional<Event> findEventById(Long reservationId);
    @Query("select r from Reservation r where r.facility.code = ?1 and r.facility.building.name = ?2 and r.date = ?3")
    List<Reservation> findByFacilityCodeAndDate(String code, String buildingName, LocalDate date);

    @Query("select count(r) from Reservation r " +
            "where r.hotUserId.id = ?1 and r.date between ?2 and ?3 and r.facility.category.name = ?4")
    long countFacilityUsageForCategory(String id, LocalDate dateStart, LocalDate dateEnd, String category);

    @Query("select r from Reservation r where r.hotUserId.id = ?1")
    List<Reservation> findReservationList(String id);

    @Query("select r from Reservation r where r.status = backend.spectrum.dguonoff.dao.model.ReservationStatus.APPROVED")
    List<Reservation> findAllApproved();

    @Query("select r.hotUserId.id from Reservation r where r.reservationId = ?1")
    Optional<String> findHostUserById(Long reservationId);

    @Query("select r.status from Reservation r where r.reservationId = ?1")
    Optional<ReservationStatus> findStatusById(Long reservationId);

    @Transactional
    @Modifying
    @Query("update Reservation r set r.status = ?1 where r.reservationId = ?2")
    int updateStatus(ReservationStatus status, Long reservationId);

    List<Reservation> findByDateAfterOrDateEqualsAndStartTimeAfter(LocalDate date, LocalDate eqDate, LocalTime time);
}
