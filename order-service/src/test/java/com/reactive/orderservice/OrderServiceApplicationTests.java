package com.reactive.orderservice;

import com.reactive.orderservice.client.ProductClient;
import com.reactive.orderservice.client.UserClient;
import com.reactive.orderservice.dto.ProductDto;
import com.reactive.orderservice.dto.PurchaseOrderRequestDto;
import com.reactive.orderservice.dto.PurchaseOrderResponseDto;
import com.reactive.orderservice.dto.UserDto;
import com.reactive.orderservice.service.OrderFulfillmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class OrderServiceApplicationTests {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderFulfillmentService service;

    @Test
    void contextLoads() {

        //zip: Los toma 1 a 1
        Flux<PurchaseOrderResponseDto> dtoFlux = Flux.zip(userClient.getAllUsers(), productClient.getAllProducts())
                .map(t -> buildDto(t.getT1(), t.getT2()))
                .flatMap(dto -> service.processOrder(Mono.just(dto)))
                .doOnNext(System.out::println);

		StepVerifier.create(dtoFlux)
				.expectNextCount(3)
				.verifyComplete();

    }

    private PurchaseOrderRequestDto buildDto(UserDto userDto, ProductDto productDto) {
        PurchaseOrderRequestDto dto = new PurchaseOrderRequestDto();
        dto.setUserId(userDto.getId());
        dto.setProductId(productDto.getId());
        return dto;
    }

}
