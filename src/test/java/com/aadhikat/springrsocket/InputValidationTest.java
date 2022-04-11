package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.Response;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InputValidationTest {

    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester requester;

    @BeforeAll
    public void setup() {
        this.requester = this.builder.transport(TcpClientTransport.create("localhost" , 6565));
    }

//    @Test
//    public void doubleItOrThrowErrorTest() {
//        Mono<Integer> mono = this.requester
//                .route("math.validation.double.it.or.throw.error.31")
//                .retrieveMono(Integer.class)
//                .doOnNext(System.out::println);
//
//        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
//    }

    @Test
    public void doubleItOrReturnEmptyTest() {
        Mono<Integer> mono = this.requester
                .route("math.validation.double.it.or.return.empty.31")
                .retrieveMono(Integer.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }

    @Test
    public void doubleItOrDefaultIfEmptyTest() {
        Mono<Integer> mono = this.requester
                .route("math.validation.double.it.or.default.if.empty.31")
                .retrieveMono(Integer.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }

    @Test
    public void doubleItOrSwitchIfEmptyTest() {
        Mono<Integer> mono = this.requester
                .route("math.validation.double.it.or.switch.if.empty.31")
                .retrieveMono(Integer.class)
                .onErrorReturn(Integer.MIN_VALUE)
                .doOnNext(System.out::println);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }

    @Test
    public void responseTest() {
        Mono<Response<Integer>> mono = this.requester
                .route("math.validation.double.response.31")
                .retrieveMono(new ParameterizedTypeReference<Response<Integer>>() {}) // Here we have to return a parameterized type reference, where we return Response<Integer>
                .doOnNext(r -> {
                    if(r.hasError())
                        System.out.println(r.getErrorResponse().getStatusCode().getDescription());
                    else
                        System.out.println(r.getSuccessResponse());
                });

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }

}
