package pl.mromasze.taccess.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mromasze.taccess.bot.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}