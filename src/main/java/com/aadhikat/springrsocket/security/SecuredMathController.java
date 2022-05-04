package com.aadhikat.springrsocket.security;

import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import com.aadhikat.springrsocket.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("math.service.secured")
public class SecuredMathController {

    @Autowired
    private MathService mathService;

    @MessageMapping("square")
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> computationRequestDtoMono , @AuthenticationPrincipal Mono<UserDetails> userDetailsMono) {
        userDetailsMono.doOnNext(System.out::println).subscribe();
        return this.mathService.findSquare(computationRequestDtoMono);
    }

    @MessageMapping("table")
    public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> computationRequestDtoMono) {
        // Here, we cannot use flatmap because, it returns a flux. When Mono becomes a Flux, use flatMapMany.
        return computationRequestDtoMono.flatMapMany(this.mathService::tableStream);
    }
}
