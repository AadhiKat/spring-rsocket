package com.aadhikat.springrsocket;

import com.aadhikat.springrsocket.dto.ChartResponseDto;
import com.aadhikat.springrsocket.dto.ComputationRequestDto;
import com.aadhikat.springrsocket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RSocketInitTest {

	//As usual, we'll be using RSocketClient,coming from rsocket.core to call the server from the client. But spring boot puts a wrapper around that.
	// It is RSocketRequester which contains RSocket client.
	// It provides a builder, which could be used for Autowiring.

	@Autowired
	private RSocketRequester.Builder builder;

	// We autowired only the builder, we need the actual requester. We'll set this as part of the beforeAll method.
	private RSocketRequester requester;

	// When using BeforeAll, don't forget to add the @TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@BeforeAll
	public void setup() {
		this.requester = this.builder.transport(TcpClientTransport.create("localhost" , 6565));
	}

	@Test
	public void fireAndForget() {
		// Here, in the RSocketrequester instance, you'll be seeing rsocket, rsocketClient instances which comes from io.rsocket.core.
		// But you'll be using the route method, since you have already created the controller with messagemapping endpoints.
		Mono<Void> mono = this.requester
				.route("math.service.print")
				.data(new ComputationRequestDto(5))
				.send();

		StepVerifier.create(mono).verifyComplete();
	}

	@Test
	public void requestResponse() {
		// Here, in the RSocketrequester instance, you'll be seeing rsocket, rsocketClient instances which comes from io.rsocket.core.
		// But you'll be using the route method, since you have already created the controller with messagemapping endpoints.
		Mono<ComputationResponseDto> mono = this.requester
				.route("math.service.square")
				.data(new ComputationRequestDto(5))
				.retrieveMono(ComputationResponseDto.class)
				.doOnNext(System.out::println);

		StepVerifier.create(mono).expectNextCount(1).verifyComplete();
	}

	@Test
	public void requestStream() {
		// Here, in the RSocketrequester instance, you'll be seeing rsocket, rsocketClient instances which comes from io.rsocket.core.
		// But you'll be using the route method, since you have already created the controller with messagemapping endpoints.
		Flux<ComputationResponseDto> flux = this.requester
				.route("math.service.table")
				.data(new ComputationRequestDto(5))
				.retrieveFlux(ComputationResponseDto.class)
				.doOnNext(System.out::println);

		StepVerifier.create(flux).expectNextCount(10).verifyComplete();
	}

	@Test
	public void requestChannel() {
		// Here, in the RSocketrequester instance, you'll be seeing rsocket, rsocketClient instances which comes from io.rsocket.core.
		// But you'll be using the route method, since you have already created the controller with messagemapping endpoints.
		Flux<ChartResponseDto> flux = this.requester
				.route("math.service.chart")
				.data(Flux.range(-10 , 21).map(ComputationRequestDto::new))
				.retrieveFlux(ChartResponseDto.class)
				.doOnNext(System.out::println);

		StepVerifier.create(flux).expectNextCount(21).verifyComplete();
	}

}
