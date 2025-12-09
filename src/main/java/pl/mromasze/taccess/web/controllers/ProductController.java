package pl.mromasze.taccess.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mromasze.taccess.bot.dto.ProductRequest;
import pl.mromasze.taccess.bot.entity.product.ChannelAccessProduct;
import pl.mromasze.taccess.bot.entity.product.Product;
import pl.mromasze.taccess.bot.entity.product.TextProduct;
import pl.mromasze.taccess.web.dto.ApiResponse;
import pl.mromasze.taccess.web.dto.ProductDTO;
import pl.mromasze.taccess.web.services.ProductService;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts()
                    .stream()
                    .map(ProductDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch products: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getAllProducts()
                    .stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found"));
            }

            return ResponseEntity.ok(ApiResponse.success(ProductDTO.fromEntity(product)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch product: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody ProductRequest request) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Product name is required"));
            }
            if (request.getPrice() == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Price is required"));
            }

            Product product;
            if ("CHANNEL_ACCESS".equals(request.getType())) {
                if (request.getChannelId() == null || request.getChannelId().trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Channel ID is required for Channel Access product"));
                }
                if (request.getDurationDays() == null || request.getDurationDays() <= 0) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Duration must be greater than 0"));
                }

                ChannelAccessProduct channelProduct = new ChannelAccessProduct();
                channelProduct.setChannelId(request.getChannelId());
                channelProduct.setDurationDays(request.getDurationDays());
                product = channelProduct;
            } else if ("TEXT".equals(request.getType())) {
                if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Content is required for Text product"));
                }

                TextProduct textProduct = new TextProduct();
                textProduct.setContent(request.getContent());
                product = textProduct;
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid product type"));
            }

            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setActive(true);

            Product savedProduct = productService.createProduct(product);
            return ResponseEntity.ok(ApiResponse.success("Product created successfully",
                    ProductDTO.fromEntity(savedProduct)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating product: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {
        try {
            Product product;
            if ("CHANNEL_ACCESS".equals(request.getType())) {
                ChannelAccessProduct channelProduct = new ChannelAccessProduct();
                channelProduct.setChannelId(request.getChannelId());
                channelProduct.setDurationDays(request.getDurationDays());
                product = channelProduct;
            } else if ("TEXT".equals(request.getType())) {
                TextProduct textProduct = new TextProduct();
                textProduct.setContent(request.getContent());
                product = textProduct;
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid product type"));
            }

            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setActive(request.getActive() != null ? request.getActive() : true);

            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(ApiResponse.success("Product updated successfully",
                    ProductDTO.fromEntity(updatedProduct)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting product: " + e.getMessage()));
        }
    }
}
