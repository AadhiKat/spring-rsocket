package com.aadhikat.springrsocket.service;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class MathClientManager {
    private final Set<RSocketRequester> requesterSet = Collections.synchronizedSet(new HashSet<>()); // Just to keep it thread safe, since it is an instance variable.

    public void add(RSocketRequester requester) {
        requester
                .rsocket()
                .onClose()
                .doFirst(() -> this.requesterSet.add(requester))
                .doFinally(s -> {
                    System.out.println("Finally");
                    this.requesterSet.remove(requester);
                })
                .subscribe();
    }

    @Scheduled(fixedRate = 1000)
    public void print() {
        System.out.println(requesterSet);
    }

    // Someone can notify all the clients in case of any updates
    public void notify(int i) {
        Flux.fromIterable(requesterSet)
                .flatMap(r -> r.route("product.updates").data(i).send())
                .subscribe();
    }
}
