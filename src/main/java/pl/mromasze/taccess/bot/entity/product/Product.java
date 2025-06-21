package pl.mromasze.taccess.bot.entity.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active;
    private String name;
    private String description;
    private BigDecimal price;

    public boolean isChannelAccessProduct() {
        return this.getClass().getSimpleName().equals("ChannelAccessProduct");
    }

    public String getProductTypeName() {
        return this.getClass().getSimpleName().equals("ChannelAccessProduct") ?
               "Dostęp do kanału" : "Tekst";
    }
}