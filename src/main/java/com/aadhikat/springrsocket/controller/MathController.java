package com.aadhikat.springrsocket.controller;

import com.aadhikat.springrsocket.dto.ChartResponseDto;
import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import com.aadhikat.springrsocket.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MathController {

    @Autowired
    private MathService mathService;

    @MessageMapping("math.service.print")
    public Mono<Void> print(Mono<ComputationRequestDto> computationRequestDtoMono) {
        return this.mathService.print(computationRequestDtoMono);
    }

    @MessageMapping("math.service.square")
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> computationRequestDtoMono) {
        return this.mathService.findSquare(computationRequestDtoMono);
    }

    @MessageMapping("math.service.table")
    public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> computationRequestDtoMono) {
        // Here, we cannot use flatmap because, it returns a flux. When Mono becomes a Flux, use flatMapMany.
        return computationRequestDtoMono.flatMapMany(this.mathService::tableStream);
    }

    @MessageMapping("math.service.chart")
    public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> computationRequestDtoFlux) {
        return this.mathService.chartStream(computationRequestDtoFlux);
    }
}
