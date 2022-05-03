package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;
import reactor.test.StepVerifier;

@SpringBootTest
public class SslTest {

    static {
        System.setProperty("javax.net.ssl.trustStore" , "/Users/aadhithyahari/IdeaProjects/spring-rsocket/src/main/resources/client.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword" , "password");
    }

    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void sslTlsTest()  {
        RSocketRequester requester = this.builder
                .transport(TcpClientTransport.create(
                        TcpClient.create().host("localhost").port(6565).secure()));

        // We'll be keep on sending the requests when the service is down. THat's the plan.
            Mono<ComputationResponseDto> responseDtoMono = requester.route("math.service.square")
                    .data(new ComputationRequestDto(5))
                    .retrieveMono(ComputationResponseDto.class)
                    .doOnNext(System.out::println);

            StepVerifier.create(responseDtoMono).expectNextCount(1).verifyComplete();

    }
}
