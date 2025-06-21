package pl.mromasze.taccess.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mromasze.taccess.bot.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(Long telegramId);
    boolean existsByTelegramId(Long telegramId);
}