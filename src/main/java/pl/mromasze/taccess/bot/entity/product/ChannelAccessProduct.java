package pl.mromasze.taccess.bot.entity.product;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("CHANNEL_ACCESS")
@Getter
@Setter
public class ChannelAccessProduct extends Product {
    private String channelId;
    private Integer durationDays;
}