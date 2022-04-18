package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"}) // This is configured to exclude the Server Auto configuration.
public class ConnectionManagerTest {

    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void connectionTest() throws InterruptedException {

        RSocketRequester requester1 = this.builder.transport(TcpClientTransport.create("localhost", 6565));
        RSocketRequester requester2 = this.builder.transport(TcpClientTransport.create("localhost", 6565));

        requester1.route("math.service.print").data(new ComputationRequestDto(5)).send().subscribe(); // Since connection is lazy, it will make the connection only when we hit the API.
        requester2.route("math.service.print").data(new ComputationRequestDto(5)).send().subscribe(); // Since connection is lazy, it will make the connection only when we hit the API.

        Thread.sleep(10000);
    }
}
