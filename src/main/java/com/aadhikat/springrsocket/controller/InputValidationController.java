package com.aadhikat.springrsocket.controller;

import com.aadhikat.springrsocket.dto.Response;
import com.aadhikat.springrsocket.dto.error.ErrorEvent;
import com.aadhikat.springrsocket.dto.error.StatusCode;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("math.validation")
public class InputValidationController {


    @MessageMapping("double.it.or.throw.error.{input}")
    public Mono<Integer> doubleItOrThrowError(@DestinationVariable int input) {
        if (input < 31)
            return Mono.just(input * 2);
        else
            return Mono.error(new IllegalArgumentException("Cannot be > 30"));
    }


    // Here, if < 31 return i * 2, otherwise it's an empty response. The client has to assume that the input is wrong.
    // The server will not be throwing error, but it would be emitting complete() signal.
    @MessageMapping("double.it.or.return.empty.{input}")
    public Mono<Integer> doubleItOrReturnEmpty(@DestinationVariable int input) {
        return Mono.just(input * 2)
                .filter(i -> i < 31)
                .map(i -> i * 2);
    }

    // Here, if < 31 return i * 2, otherwise it's an empty response. The client has to assume that the input is wrong.
    // The server will not be throwing error, but it would be emitting complete() signal.
    @MessageMapping("double.it.or.default.if.empty.{input}")
    public Mono<Integer> doubleItOrDefaultIfEmpty(@DestinationVariable int input) {
        return Mono.just(input * 2)
                .filter(i -> i < 31)
                .map(i -> i * 2)
                .defaultIfEmpty(Integer.MIN_VALUE);
    }

    // When returning Mono.error in the server side, the client needs to handle it with onErrorReturn method, with may be a default value.
    @MessageMapping("double.it.or.switch.if.empty.{input}")
    public Mono<Integer> doubleItOrSwitchIfEmpty(@DestinationVariable int input) {
        return Mono.just(input * 2)
                .filter(i -> i < 31)
                .map(i -> i * 2)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("cannot be > 30")));
    }

    @MessageMapping("double.response.{input}")
    public Mono<Response<Integer>> doubleResponse(@DestinationVariable int input) {
        return Mono.just(input * 2)
                .filter(i -> i < 31)
                .map(i -> i * 2)
                .map(Response::with)
                .defaultIfEmpty(Response.with(new ErrorEvent(StatusCode.EC001)));
    }


    //@MessageExceptionHandler(IllegalArgumentException.class)
    @MessageExceptionHandler
    public Mono<Integer> handleException(Exception e) {
        return Mono.just(-1);
    }

}
