package com.aadhikat.springrsocket.controller;

import com.aadhikat.springrsocket.dto.ClientConnectionRequest;
import com.aadhikat.springrsocket.service.MathClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ConnectionHandler {

    @Autowired
    private MathClientManager clientManager;

//    @ConnectMapping
//    public Mono<Void> handleConnection(ClientConnectionRequest request, RSocketRequester requester) {
//        System.out.println("Connection setup : " + request);
//        return request.getSecretKey().equals("password") ? Mono.empty() : Mono.fromRunnable(() -> requester.rsocketClient().dispose());
//    }

    @ConnectMapping
    public Mono<Void> handleConnection( RSocketRequester requester) {
        System.out.println("Connection setup ");
        return Mono.fromRunnable(() -> this.clientManager.add(requester));
    }
}
