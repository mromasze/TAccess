package pl.mromasze.taccess.bot.entity.product;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("TEXT")
@Getter
@Setter
public class TextProduct extends Product {
    private String content;
}