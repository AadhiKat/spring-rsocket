package com.aadhikat.springrsocket;

import ch.qos.logback.core.net.server.Client;
import com.aadhikat.springrsocket.dto.ClientConnectionRequest;
import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionSetupTest {

    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester requester;

    @BeforeAll
    public void setup() {

        ClientConnectionRequest request = new ClientConnectionRequest();
        request.setClientId("order-service");
        request.setSecretKey("password");
        this.requester = this
                .builder
                .setupData(request)
                .transport(TcpClientTransport.create("localhost" , 6565));
    }

    @RepeatedTest(3)
    public void connectionTest() {
        Mono<ComputationResponseDto> responseDtoMono = this.requester.route("math.service.square")
                .data(new ComputationRequestDto(ThreadLocalRandom.current().nextInt(1, 50)))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseDtoMono).expectNextCount(1).verifyComplete();
    }
}
