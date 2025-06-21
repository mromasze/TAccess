package pl.mromasze.taccess.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mromasze.taccess.bot.dto.ProductRequest;
import pl.mromasze.taccess.bot.entity.product.ChannelAccessProduct;
import pl.mromasze.taccess.bot.entity.product.Product;
import pl.mromasze.taccess.bot.entity.product.TextProduct;
import pl.mromasze.taccess.web.services.ProductService;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nazwa produktu jest wymagana");
            }
            if (request.getPrice() == null) {
                return ResponseEntity.badRequest().body("Cena jest wymagana");
            }

            Product product;
            if ("CHANNEL_ACCESS".equals(request.getType())) {
                if (request.getChannelId() == null || request.getChannelId().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("ID kanału jest wymagane dla produktu typu Channel Access");
                }
                if (request.getDurationDays() == null || request.getDurationDays() <= 0) {
                    return ResponseEntity.badRequest().body("Czas dostępu musi być większy niż 0");
                }

            ChannelAccessProduct channelProduct = new ChannelAccessProduct();
            channelProduct.setChannelId(request.getChannelId());
            channelProduct.setDurationDays(request.getDurationDays());
            product = channelProduct;
        } else if ("TEXT".equals(request.getType())) {
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Treść jest wymagana dla produktu typu Text");
            }

            TextProduct textProduct = new TextProduct();
            textProduct.setContent(request.getContent());
            product = textProduct;
        } else {
            return ResponseEntity.badRequest().body("Nieprawidłowy typ produktu");
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setActive(true);

        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(savedProduct);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Błąd podczas tworzenia produktu: " + e.getMessage());
    }
}
}