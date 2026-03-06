package org.example.flux_mono.exercice13;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<Flux<Product>>> searchProductsByName(@RequestParam String name) {
        return Mono.defer(() -> Mono.just(ResponseEntity.ok(productService.searchByName(name))))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Product>> createProduct(@RequestBody Product product) {
        return Mono.defer(() -> productService.create(product))
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return Mono.defer(() -> productService.update(id, product))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        return productService.findById(id)
                .flatMap(existing -> productService.deleteById(id)
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/buy")
    public Mono<ResponseEntity<Object>> buyProduct(@PathVariable Long id, @RequestParam int quantity) {
        return productService.purchase(id, quantity)
                .map(product -> ResponseEntity.ok().<Object>body(product))
                .onErrorResume(ProductNotFoundException.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage())))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().body(ex.getMessage())))
                .onErrorResume(InsufficientStockException.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage())));
    }
}
