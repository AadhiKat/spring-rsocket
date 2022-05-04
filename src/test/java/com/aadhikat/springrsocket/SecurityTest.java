package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import io.rsocket.RSocket;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityTest {

    private RSocketRequester requester;
    private final MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());

    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void connectionSetup() {

        UsernamePasswordMetadata metadata = new UsernamePasswordMetadata("client" , "password");

        this.requester = this.builder
                .setupMetadata(metadata , mimeType)
                .transport(TcpClientTransport.create("localhost" , 6565));

    }

    @Test
    public void requestResponse() {

        UsernamePasswordMetadata metadata = new UsernamePasswordMetadata("user" , "password");

        Mono<ComputationResponseDto> mono = requester
                .route("math.service.secured.square")
                .metadata(metadata, mimeType)
                .data(new ComputationRequestDto(5))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }

    @Test
    public void requestStream() {

        UsernamePasswordMetadata metadata = new UsernamePasswordMetadata("admin" , "password");

        Flux<ComputationResponseDto> flux = this.requester
                .route("math.service.secured.table")
                .metadata(metadata, mimeType)
                .data(new ComputationRequestDto(5))
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println)
                .take(3);

        StepVerifier.create(flux).expectNextCount(3).verifyComplete();
    }
}
