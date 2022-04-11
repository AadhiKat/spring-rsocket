package com.aadhikat.guesser;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

// Here, the important thing is, how we are going to emit? For emitting values programatically, we'll use Sinks.
public class Player {

    private final Sinks.Many<Integer> sink = Sinks.many().unicast().onBackpressureBuffer();
    private int lower = 0;
    private int upper = 100;
    private int mid = 0;
    private int attempts = 0;

    public Flux<Integer> guesses() {
        return this.sink.asFlux();
    }

    public void play() {
        this.emit();
    }

    public Consumer<GuessNumberResponse> receives() {
        return this::processResponse;
    }

    private void processResponse(GuessNumberResponse guessNumberResponse) {
        attempts++;
        System.out.println(attempts + " : " + mid + " : " + guessNumberResponse);

        if (GuessNumberResponse.EQUAL.equals(guessNumberResponse)) {
            this.sink.tryEmitComplete();
            return;
        }

        if (GuessNumberResponse.GREATER.equals(guessNumberResponse))
            lower = mid;
        else if (GuessNumberResponse.LESSER.equals(guessNumberResponse))
            upper = mid;
        this.emit();
    }

    private void emit() {
        mid = lower + (upper - lower) / 2;
        this.sink.tryEmitNext(mid);
    }
}
