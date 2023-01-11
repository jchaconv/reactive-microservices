package com.reactive.userservice.service;

import com.reactive.userservice.dto.UserDto;
import com.reactive.userservice.repository.UserRepository;
import com.reactive.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public Flux<UserDto> getAll() {
        return repository.findAll()
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> getUserById(final int userId) {
        return repository.findById(userId)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> createUser(Mono<UserDto> userDtoMono) {
        return userDtoMono.map(EntityDtoUtil::toEntity)
                .flatMap(repository::save)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> updateUser(int id, Mono<UserDto> userDtoMono) {
        return repository.findById(id)
                .flatMap(u -> userDtoMono
                                .map(EntityDtoUtil::toEntity)
                                .doOnNext(e -> e.setId(id)))
                .flatMap(repository::save)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<Void> deleteUser(int id) {
        return repository.deleteById(id);
    }


}
