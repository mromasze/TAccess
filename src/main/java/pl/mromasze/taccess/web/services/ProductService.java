package pl.mromasze.taccess.web.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mromasze.taccess.bot.entity.product.Product;
import pl.mromasze.taccess.bot.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (existingProduct.getClass() != updatedProduct.getClass()) {
            throw new RuntimeException("Cannot change product type");
        }

        updatedProduct.setId(id);
        return productRepository.save(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}