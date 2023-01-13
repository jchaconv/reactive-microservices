package com.reactive.orderservice.service;

import com.reactive.orderservice.dto.PurchaseOrderResponseDto;
import com.reactive.orderservice.entity.PurchaseOrder;
import com.reactive.orderservice.repository.PurchaseOrderRepository;
import com.reactive.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderQueryService {

    @Autowired
    private PurchaseOrderRepository repository;

    public Flux<PurchaseOrderResponseDto> getProductsByUserId(int userId) {
        return Flux.fromStream( () -> repository.findByUserId(userId).stream()) //blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }



}
