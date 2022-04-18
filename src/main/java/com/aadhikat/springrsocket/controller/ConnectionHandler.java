package com.aadhikat.springrsocket.controller;

import com.aadhikat.springrsocket.dto.ClientConnectionRequest;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ConnectionHandler {

    @ConnectMapping
    public Mono<Void> handleConnection(ClientConnectionRequest request) {
        System.out.println("Connection setup : " + request);
        return request.getSecretKey().equals("password") ? Mono.empty() : Mono.error(new RuntimeException("invalid credentials"));
    }
}
