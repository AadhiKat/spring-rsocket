package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

@SpringBootTest
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"})
// This is configured to exclude the Server Auto configuration.
public class ConnectionRetryTest {

    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void connectionTest() throws InterruptedException {
        RSocketRequester requester = this.builder
                .rsocketConnector(c -> c
                        .reconnect(Retry.fixedDelay(10, Duration.ofSeconds(2))
                        .doBeforeRetry(s -> System.out.println("Retrying : " + s.totalRetriesInARow()))
                        ))
                .transport(TcpClientTransport.create("localhost", 6565));

        // We'll be keep on sending the requests when the service is down. THat's the plan.
        for (int i = 0; i < 50; i++) {
            Mono<ComputationResponseDto> responseDtoMono = requester.route("math.service.print")
                    .data(new ComputationRequestDto(i))
                    .retrieveMono(ComputationResponseDto.class)
                    .doOnNext(System.out::println);

            StepVerifier.create(responseDtoMono).expectNextCount(1).verifyComplete();

            Thread.sleep(2000);
        }
    }
}
