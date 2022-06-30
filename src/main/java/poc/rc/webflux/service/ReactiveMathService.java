package poc.rc.webflux.service;

import java.time.Duration;
import org.springframework.stereotype.Service;
import poc.rc.webflux.SleepUtil;
import poc.rc.webflux.dto.MultiplyRequestDto;
import poc.rc.webflux.dto.Response;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveMathService {

  public Mono<Response> findSquare(int input) {
    return Mono.fromSupplier(() -> input * input)
        .map(Response::new);
  }

  public Flux<Response> multiplicationTable(int input) {
    return Flux.range(1, 10)
        .delayElements(Duration.ofSeconds(1)) //non-blocking sleep
//        .doOnNext(i -> SleepUtil.sleepSeconds(1)) //blocking sleep
        .doOnNext(i -> System.out.println("Reactive Math processing: " + i))
        .map(i -> new Response(i * input));
  }

  public Mono<Response> multiply(Mono<MultiplyRequestDto> req) {
    return req.map(dto -> dto.getFirst() * dto.getSecond())
        .map(Response::new);
  }

}
