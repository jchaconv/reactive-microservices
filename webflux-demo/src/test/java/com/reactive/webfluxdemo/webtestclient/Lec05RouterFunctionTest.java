package com.reactive.webfluxdemo.webtestclient;

import com.reactive.webfluxdemo.config.RequestHandler;
import com.reactive.webfluxdemo.config.RouterConfig;
import com.reactive.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = RouterConfig.class)
public class Lec05RouterFunctionTest {

    private WebTestClient client;

    @Autowired
    private RouterConfig config;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private RequestHandler requestHandler;

    @BeforeAll
    public void setClient() {
        //this.client = WebTestClient.bindToRouterFunction(config.highLevelRouter()).build();
        this.client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void test() {

        Mockito.when(requestHandler.squareHandler(Mockito.any())).thenReturn(ServerResponse.ok().bodyValue(new Response(225)));

        this.client
                .get()
                .uri("/router/square/{input}", 15)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(225));

    }


}
