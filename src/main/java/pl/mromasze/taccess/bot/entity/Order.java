package pl.mromasze.taccess.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.mromasze.taccess.bot.entity.product.Product;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDateTime purchaseDate;
    private LocalDateTime expiryDate;
}
