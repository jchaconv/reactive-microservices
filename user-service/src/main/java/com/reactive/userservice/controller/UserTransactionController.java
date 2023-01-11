package com.reactive.userservice.controller;

import com.reactive.userservice.dto.TransactionRequestDto;
import com.reactive.userservice.dto.TransactionResponseDto;
import com.reactive.userservice.entity.UserTransaction;
import com.reactive.userservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user/transaction")
public class UserTransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public Mono<TransactionResponseDto> createTransaction(@RequestBody Mono<TransactionRequestDto> requestDtoMono) {
        return requestDtoMono.flatMap(service::createTransaction);
    }

    @GetMapping
    public Flux<UserTransaction> getByUserId(@RequestParam("userId") int userId) {
        return service.getByUserId(userId);
    }


}
