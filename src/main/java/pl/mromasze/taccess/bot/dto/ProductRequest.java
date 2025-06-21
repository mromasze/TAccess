package pl.mromasze.taccess.bot.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String type;
    private String name;
    private BigDecimal price;
    private boolean active;

    private String channelId;
    private Integer durationDays;

    private String content;
}