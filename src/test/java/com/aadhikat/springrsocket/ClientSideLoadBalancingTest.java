package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.loadbalance.RoundRobinLoadbalanceStrategy;
import io.rsocket.loadbalance.WeightedLoadbalanceStrategy;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootTest
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"}) // This is configured to exclude the Server Auto configuration.
public class ClientSideLoadBalancingTest {

    @Autowired
    private Flux<List<LoadbalanceTarget>> targets;

    @Autowired
    private RSocketRequester.Builder builder;

    // Here, we have to use transports, which accepts a Publisher.
    @Test
    public void clientSideLoadBalancingTest() throws InterruptedException {


        // This weighted load balance strategy actually, it depends on the latency from the instances. It adds some weight based on the latency.
        // It is very helpful in multi-az deployments.
        RSocketRequester requester = this.builder.transports(targets, WeightedLoadbalanceStrategy.create());


        //RSocketRequester requester = this.builder.transports(targets, new RoundRobinLoadbalanceStrategy());

        for (int i = 0; i < 50; i++) {
            requester.route("math.service.print").data(new ComputationRequestDto(i)).send().subscribe(); // Since connection is lazy, it will make the connection only when we hit the API.
            Thread.sleep(2000);
        }
    }
}
