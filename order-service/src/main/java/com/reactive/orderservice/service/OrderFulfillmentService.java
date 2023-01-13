package com.reactive.orderservice.service;

import com.reactive.orderservice.client.ProductClient;
import com.reactive.orderservice.client.UserClient;
import com.reactive.orderservice.dto.PurchaseOrderRequestDto;
import com.reactive.orderservice.dto.PurchaseOrderResponseDto;
import com.reactive.orderservice.dto.RequestContext;
import com.reactive.orderservice.repository.PurchaseOrderRepository;
import com.reactive.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class OrderFulfillmentService {

    @Autowired
    private PurchaseOrderRepository repository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {
        return requestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map(repository::save) //blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<RequestContext> productRequestResponse(RequestContext requestContext) {
        return productClient.getProductById(requestContext.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(requestContext::setProductDto)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .thenReturn(requestContext);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext requestContext) {
        return userClient.authorizeTransaction(requestContext.getTransactionRequestDto())
                .doOnNext(requestContext::setTransactionResponseDto)
                .thenReturn(requestContext);
    }


}
