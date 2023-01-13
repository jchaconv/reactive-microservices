package com.reactive.productservice.controller;

import com.reactive.productservice.dto.ProductDto;
import com.reactive.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("all")
    public Flux<ProductDto> all() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable String id) {
        simulateRandomException();
        return service.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("price-range")
    public Flux<ProductDto> getByPriceRange(@RequestParam int min,
                                            @RequestParam int max) {
        return service.getProductByPriceRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> insertProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        return service.insertProduct(productDtoMono);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> updateProduct(@PathVariable String id,
                                                          @RequestBody Mono<ProductDto> productDtoMono) {

        return service.updateProduct(id, productDtoMono)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return service.deleteProduct(id);
    }

    private void simulateRandomException() {
        int nextInt = ThreadLocalRandom.current().nextInt(1, 10);
        if(nextInt > 5)
            throw new RuntimeException("something is wrong");
    }

}
