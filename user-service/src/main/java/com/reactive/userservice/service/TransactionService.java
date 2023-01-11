package com.reactive.userservice.service;

import com.reactive.userservice.dto.TransactionRequestDto;
import com.reactive.userservice.dto.TransactionResponseDto;
import com.reactive.userservice.dto.TransactionStatus;
import com.reactive.userservice.entity.UserTransaction;
import com.reactive.userservice.repository.UserRepository;
import com.reactive.userservice.repository.UserTransactionRepository;
import com.reactive.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTransactionRepository transactionRepository;

    public Mono<TransactionResponseDto> createTransaction(final TransactionRequestDto requestDto) {
        return userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
                    .filter(Boolean::booleanValue)
                    .map(b -> EntityDtoUtil.toEntity(requestDto))
                    .flatMap(transactionRepository::save)
                    .map(ut -> EntityDtoUtil.toDto(requestDto, TransactionStatus.APPROVED))
                    .defaultIfEmpty(EntityDtoUtil.toDto(requestDto, TransactionStatus.DECLINED));
    }

    public Flux<UserTransaction> getByUserId(int userId) {
        return transactionRepository.findByUserId(userId);
    }

}
