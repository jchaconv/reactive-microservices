package com.reactive.webfluxdemo.service;

import com.reactive.webfluxdemo.dto.MultiplyRequestDto;
import com.reactive.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReactiveMathService {

    public Mono<Response> findSquare(int input) {
        return Mono.fromSupplier( () -> input * input)
                .map(Response::new);
    }

    public Flux<Response> multiplicationTable(int input) {

        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                //.doOnNext(i -> SleepUtil.sleepSeconds(1))
                .doOnNext(i -> System.out.println("reactive-math-service processing: " + i))
                .map(i -> new Response(i * input));

        /*
        Esta es una mala práctica, no se debería usar porque se seguiría ejecutando
        incluso cuando ya ha sido cancelada la solicitud:

        List<Response> list = IntStream.rangeClosed(1, 10)
                .peek(i -> SleepUtil.sleepSeconds(1))
                .peek(i -> System.out.println("math-service processing: " + i))
                .mapToObj(i -> new Response(i * input))
                .collect(Collectors.toList());

        return Flux.fromIterable(list);
        */
    }

    public Mono<Response> multiply(Mono<MultiplyRequestDto> dtoMono) {
        return dtoMono
                .map(dto -> dto.getFirst() * dto.getSecond())
                .map(Response::new);
    }



}
