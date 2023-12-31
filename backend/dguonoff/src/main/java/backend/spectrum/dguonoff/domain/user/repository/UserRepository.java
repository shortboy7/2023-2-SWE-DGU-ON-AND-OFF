package backend.spectrum.dguonoff.domain.user.repository;

import backend.spectrum.dguonoff.dao.User;
import backend.spectrum.dguonoff.dao.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u from User u where u.sid = ?1")
    Optional<User> findBySid(String sid);
    @Transactional
    @Modifying
    @Query("update User u set u.role = 'ADMIN' where u.id = ?1")
    int changeRoleToAdmin(@NonNull String id);

    @Transactional
    @Modifying
    @Query("update User u set u.role = 'NORMAL' where u.id = ?1")
    int changeRoleToNormal(@NonNull String id);

    @Query("select u.role from User u where u.id = ?1")
    Optional<Role> findRoleById(String id);

}
