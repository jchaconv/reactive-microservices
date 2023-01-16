package com.reactive.webfluxdemo.webtestclient;

import com.reactive.webfluxdemo.controller.ParamsController;
import com.reactive.webfluxdemo.controller.ReactiveMathController;
import com.reactive.webfluxdemo.dto.Response;
import com.reactive.webfluxdemo.service.ReactiveMathService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

//@WebFluxTest(ReactiveMathController.class)
@WebFluxTest(controllers = {ReactiveMathController.class, ParamsController.class})
public class Lec02ControllerGetTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReactiveMathService service;

    @Test
    public void fluentAssertionTest() {

        //Mockito.when(service.findSquare(Mockito.anyInt())).thenReturn(Mono.just(new Response(25)));
        Mockito.when(service.findSquare(Mockito.anyInt())).thenReturn(Mono.empty());

        this.client
                .get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                //.value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25))
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(-1));

    }

    @Test
    public void listResponseTest() {

        Flux<Response> flux = Flux.range(1, 3).map(Response::new);

        Mockito.when(service.multiplicationTable(Mockito.anyInt())).thenReturn(flux);

        this.client
                .get()
                .uri("/reactive-math/table/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Response.class)
                .hasSize(3);

    }

    @Test
    public void streamingResponseTest() {

        Flux<Response> flux = Flux.range(1, 3).map(Response::new).delayElements(Duration.ofMillis(100));

        Mockito.when(service.multiplicationTable(Mockito.anyInt())).thenReturn(flux);

        this.client
                .get()
                .uri("/reactive-math/table/{number}/stream", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(Response.class)
                .hasSize(3);

    }

    @Test
    public void paramsTest() {

        Map<String, Integer> map = Map.of("count", 10, "page", 20);

        this.client
                .get()
                .uri(b -> b.path("/jobs/search").query("count={count}&page={page}").build(map))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(2).contains(10, 20);


    }


}
