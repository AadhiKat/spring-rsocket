package com.aadhikat.springrsocket.controller;

import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ConnectionHandler {

    @ConnectMapping
    public Mono<Void> handleConnection() {
        System.out.println("Connection setup");
        return Mono.empty();
    }
}
