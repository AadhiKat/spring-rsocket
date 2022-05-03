package com.aadhikat.springrsocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
public class BatchJobController {

    @MessageMapping("batch.job.request")
    public Mono<Void> submitJob(Mono<Integer> integerMono, RSocketRequester requester) {
        this.process(integerMono, requester);
        return Mono.empty();
    }

    //This is executed in an asynchronous - non-blocking way
    private void process(Mono<Integer> integerMono, RSocketRequester requester) {
        integerMono
                .delayElement(Duration.ofSeconds(10))
                .map(i -> i * i * i)
                .flatMap(i -> requester.route("batch.job.response")
                        .data(i)
                        .send()
                )
                .subscribe();
    }
}

