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

    public Flux<Product> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return Flux.error(new IllegalArgumentException("Name is required"));
        }
        return productRepository.findByNameContainingIgnoreCase(name);
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

        return productRepository.decrementStockIfAvailable(id, quantity)
                .flatMap(updatedRows -> {
                    if (updatedRows != null && updatedRows > 0) {
                        return productRepository.findById(id);
                    }

                    return productRepository.existsById(id)
                            .flatMap(exists -> {
                                if (!exists) {
                                    return Mono.error(new ProductNotFoundException("Product not found"));
                                }
                                return Mono.error(new InsufficientStockException("Insufficient stock"));
                            });
                });
    }

    private void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
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
