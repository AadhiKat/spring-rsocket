package com.aadhikat.guesser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class GuessNumberController {

    @Autowired
    GuessNumberService service;

    @MessageMapping("guess.a.number")
    public Flux<GuessNumberResponse> play(Flux<Integer> flux) {
        return this.service.play(flux);
    }
}
