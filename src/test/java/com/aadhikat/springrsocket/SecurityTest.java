package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import io.rsocket.RSocket;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class SecurityTest {

    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void requestResponse() {

        UsernamePasswordMetadata metadata = new UsernamePasswordMetadata("client" , "password");
        MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());


        // This won't work if server does not have a decoder for the MimeType
        RSocketRequester requester = this.builder
                .setupMetadata(metadata , mimeType)
                .transport(TcpClientTransport.create("localhost" , 6565));
        // Here, in the RSocketrequester instance, you'll be seeing rsocket, rsocketClient instances which comes from io.rsocket.core.
        // But you'll be using the route method, since you have already created the controller with messagemapping endpoints.
        Mono<ComputationResponseDto> mono = requester
                .route("math.service.secured.square")
                .data(new ComputationRequestDto(5))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }
}
