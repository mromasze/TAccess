package pl.mromasze.taccess.web.dto;

import pl.mromasze.taccess.bot.entity.product.ChannelAccessProduct;
import pl.mromasze.taccess.bot.entity.product.Product;
import pl.mromasze.taccess.bot.entity.product.TextProduct;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean active;
    private String type;
    private String channelId;
    private Integer durationDays;
    private String content;

    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setActive(product.isActive());

        if (product instanceof ChannelAccessProduct) {
            ChannelAccessProduct cap = (ChannelAccessProduct) product;
            dto.setType("CHANNEL_ACCESS");
            dto.setChannelId(cap.getChannelId());
            dto.setDurationDays(cap.getDurationDays());
        } else if (product instanceof TextProduct) {
            TextProduct tp = (TextProduct) product;
            dto.setType("TEXT");
            dto.setContent(tp.getContent());
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
