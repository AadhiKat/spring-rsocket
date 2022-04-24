package com.aadhikat.springrsocket.service;

import com.aadhikat.springrsocket.dto.ChartResponseDto;
import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MathService {
    //ff
    public Mono<Void> print(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono.doOnNext(System.out::println).then();
    }

    //rr -> No need for payload, ObjectUtil serialization, deserialization etc.
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono.map(ComputationRequestDto::getInput).map(i -> new ComputationResponseDto(i, i * i));
    }

    //rs
    public Flux<ComputationResponseDto> tableStream(ComputationRequestDto requestDto) {
        return Flux
                .range(1, 1000)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> new ComputationResponseDto(requestDto.getInput(), requestDto.getInput() * i));
    }

    //rc
    public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> requestDtoFlux) {
        return requestDtoFlux.map(ComputationRequestDto::getInput).map(i -> new ChartResponseDto(i, (i * i) + 1));
    }
}
