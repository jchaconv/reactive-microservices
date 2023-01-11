package com.reactive.userservice.util;

import com.reactive.userservice.dto.TransactionRequestDto;
import com.reactive.userservice.dto.TransactionResponseDto;
import com.reactive.userservice.dto.TransactionStatus;
import com.reactive.userservice.dto.UserDto;
import com.reactive.userservice.entity.User;
import com.reactive.userservice.entity.UserTransaction;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class EntityDtoUtil {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        return user;
    }

    public static UserTransaction toEntity(TransactionRequestDto requestDto) {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(requestDto.getUserId());
        userTransaction.setAmount(requestDto.getAmount());
        userTransaction.setTransactionDate(LocalDateTime.now());
        return userTransaction;
    }

    public static TransactionResponseDto toDto(TransactionRequestDto requestDto,
                                               TransactionStatus status) {
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setUserId(requestDto.getUserId());
        responseDto.setAmount(requestDto.getAmount());
        responseDto.setStatus(status);
        return responseDto;
    }


}
