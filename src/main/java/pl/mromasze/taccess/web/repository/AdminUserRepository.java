package pl.mromasze.taccess.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mromasze.taccess.web.entity.AdminUser;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsername(String username);
    boolean existsByUsername(String username);
}