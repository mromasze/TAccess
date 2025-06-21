package pl.mromasze.taccess.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mromasze.taccess.bot.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}