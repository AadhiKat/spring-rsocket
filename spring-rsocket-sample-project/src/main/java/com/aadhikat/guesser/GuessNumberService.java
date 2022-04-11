package com.aadhikat.guesser;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class GuessNumberService {

    // The game starts the first time this method is invoked. The server has to think the number.
    public Flux<GuessNumberResponse> play(Flux<Integer> flux) {

        int serverNumber = ThreadLocalRandom.current().nextInt(1, 100);
        System.out.println("Server number : " + serverNumber);
        return flux.map(i -> this.compare(serverNumber, i));
    }

    private GuessNumberResponse compare(int serverNumber, int clientNumber) {
        System.out.println("Server Number : " + serverNumber + " Client Number " + clientNumber);
        return serverNumber > clientNumber ? GuessNumberResponse.GREATER : serverNumber < clientNumber ? GuessNumberResponse.LESSER : GuessNumberResponse.EQUAL;
    }
}
