package com.aadhikat.springrsocket.client.config;

import com.aadhikat.springrsocket.client.serviceregistry.RSocketServerInstance;
import com.aadhikat.springrsocket.client.serviceregistry.ServiceRegistryClient;
import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.transport.ClientTransport;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Configuration
public class LoadBalanceTargetConfig {

    @Autowired
    private ServiceRegistryClient registryClient;

    // The idea is, the service registry might keep on emitting the updated server instances.
    // So we can periodically query the updated service instances.
    // If you go to server and kill the server, the load is balanced with the other machines as well.

    @Bean
    public Flux<List<LoadbalanceTarget>> targetsFlux () {
        return Flux.from(targets());
    }




    // 2nd experiment -> Now, we'll try to periodically emit the instances using refresh.
    // This will periodically refresh the list and since it removes one among them, the connection rebalances itself.
//    @Bean
//    public Flux<List<LoadbalanceTarget>> requester() {
//        return Flux.interval(Duration.ofSeconds(5))
//                .flatMap(i -> targets())
//                .doOnNext(l -> l.remove(ThreadLocalRandom.current().nextInt(0 , 3)));
//    }




    // Here we have to convert the load balancer instances to LoadbalanceTarget class in RSocket.
    // When converting, you have to provide a unique key.
    // For the client transport, we'll use the tcp client transport as usual.
    private Mono<List<LoadbalanceTarget>> targets() {
        return Mono.fromSupplier(() ->
                this.registryClient.getInstanceList()
                        .stream()
                        .map(server ->
                                LoadbalanceTarget.from(getKey(server),
                                        getClienttransport(server))
                        )
                        .collect(Collectors.toList()));
    }

    private String getKey(RSocketServerInstance instance) {
        return instance.getHost() + instance.getPort();
    }

    private ClientTransport getClienttransport(RSocketServerInstance instance) {
        return TcpClientTransport.create(instance.getHost(), instance.getPort());
    }
}
