package org.example.flux_mono.exercice13;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Flux<Product> findAvailable() {
        return productRepository.findByStockGreaterThan(0);
    }

    public Mono<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Mono<Product> create(Product product) {
        validate(product);
        product.setId(null);
        return productRepository.save(product);
    }

    public Mono<Product> update(Long id, Product product) {
        validate(product);
        return productRepository.findById(id)
                .flatMap(existing -> {
                    existing.setName(product.getName());
                    existing.setDescription(product.getDescription());
                    existing.setPrice(product.getPrice());
                    existing.setStock(product.getStock());
                    return productRepository.save(existing);
                });
    }

    public Mono<Void> deleteById(Long id) {
        return productRepository.deleteById(id);
    }

    public Mono<Product> purchase(Long id, int quantity) {
        if (quantity <= 0) {
            return Mono.error(new IllegalArgumentException("Quantity must be greater than 0"));
        }

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")))
                .flatMap(product -> {
                    if (product.getStock() < quantity) {
                        return Mono.error(new IllegalStateException("Insufficient stock"));
                    }
                    product.setStock(product.getStock() - quantity);
                    return productRepository.save(product);
                });
    }

    private void validate(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be >= 0");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock must be >= 0");
        }
    }
}
