package com.aadhikat.guesser;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessNumberTest {

    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester requester;

    @BeforeAll
    public void setup() {
        this.requester = this
                .builder
                .transport(TcpClientTransport.create("localhost" , 6565));
    }

    @Test
    public void guessNumberTest()  {
        Player player = new Player();

        Mono<Void> mono = this.requester.route("guess.a.number")
                .data(player.guesses().delayElements(Duration.ofSeconds(1)))
                .retrieveFlux(GuessNumberResponse.class)
                .doOnNext(player.receives())
                .doFirst(player::play)
                .then();

        StepVerifier.create(mono).verifyComplete();

    }

}
