package com.aadhikat.springrsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


// Now, if you remember our simple RSocket project, the client had created the asychronous implementation of RSocket, and we provided the SocketAcceptor with CallbackService.
// Here, similarly we have to share our controller with the server
// To do that, we can autowire the RSocketMessageHandler, which basically looks for all the @Controller annotation and @MessageMapping annotation and based on that, they create the SocketAcceptor
// handler.responder() -> will give you the SocketAcceptor

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CallbackTest {

    @Autowired
    private RSocketRequester.Builder builder;

    @Autowired
    private RSocketMessageHandler messageHandler;

    private RSocketRequester requester;

    @BeforeAll
    public void setup() {
        this.requester = this
                .builder
                .rsocketConnector(c -> c.acceptor(messageHandler.responder()))
                .transport(TcpClientTransport.create("localhost" , 6565));
    }

    @Test
    public void callbackTest() throws InterruptedException {
        Mono<Void> mono = this.requester
                .route("batch.job.request")
                .data(5)
                .send();

        StepVerifier.create(mono).verifyComplete();

        Thread.sleep(12000); // In order for the server to send us the response, the client should also be up and running.
    }
}
