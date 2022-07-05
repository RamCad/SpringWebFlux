package poc.rc.webflux;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import org.springframework.web.util.UriComponentsBuilder;
import poc.rc.webflux.dto.InputFailedValidation;
import poc.rc.webflux.dto.MultiplyRequestDto;
import poc.rc.webflux.dto.Response;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class WF01GetSingleResTest extends BaseTest {

  @Autowired
  private WebClient webClient;

  @Test
  public void blockTest() {
    Response response = webClient.get()
        .uri("/reactive-math/square/{input}", 5)
        .retrieve()
        .bodyToMono(Response.class)
        .block();
    System.out.println(response);
  }

  @Test
  public void stepVerifierTest() {
    Mono<Response> response = webClient.get()
        .uri("/reactive-math/square/{input}", 5)
        .retrieve()
        .bodyToMono(Response.class);

    StepVerifier.create(response)
        .expectNextMatches(response1 -> response1.getOutput() == 25)
        .verifyComplete();
  }

  @Test
  public void fluxTest() {
    Flux<Response> response = webClient.get()
        .uri("/reactive-math/table/{input}", 5)
        .retrieve()
        .bodyToFlux(Response.class);

    StepVerifier.create(response)
        .expectNextCount(10)
        .verifyComplete();
  }

  @Test
  public void fluxStreamTest() {
    Flux<Response> response = webClient.get()
        .uri("/reactive-math/table/{input}/stream", 5)
        .retrieve()
        .bodyToFlux(Response.class)
        .doOnNext(System.out::println);

    StepVerifier.create(response)
        .expectNextCount(10)
        .verifyComplete();
  }

  @Test
  public void postTest() {
    Mono<Response> response = webClient.post()
        .uri("/reactive-math/multiply")
        .bodyValue(buildReq(2, 3))
        .retrieve()
        .bodyToMono(Response.class)
        .doOnNext(System.out::println);

    StepVerifier.create(response)
        .expectNextMatches(response1 -> response1.getOutput() == 6)
        .verifyComplete();
  }

  private MultiplyRequestDto buildReq(int a, int b) {
    return MultiplyRequestDto.builder()
        .first(a)
        .second(b)
        .build();
  }

  @Test
  public void headersTest() {
    Mono<Response> response = webClient.post()
        .uri("/reactive-math/multiply")
        .bodyValue(buildReq(2, 3))
//        .header("h1", "header1")
//        .headers(httpHeaders -> httpHeaders.set("h2", "header2"))
        .headers(httpHeaders -> httpHeaders.setBasicAuth("username", "password"))
        .retrieve()
        .bodyToMono(Response.class)
        .doOnNext(System.out::println);

    StepVerifier.create(response)
        .expectNextMatches(response1 -> response1.getOutput() == 6)
        .verifyComplete();
  }

  @Test
  public void badReqTest() {
    Mono<Response> response = webClient.get()
        .uri("/reactive-math/square/{input}/throw", 5)
        .retrieve()
        .bodyToMono(Response.class)
        .doOnError(throwable -> System.out.println(throwable.getMessage()));

    StepVerifier.create(response)
        .verifyError(BadRequest.class);
  }

  // exchange = retrieve + additional info (http status code)
  @Test
  public void exchangeBadReqTest() {
    Mono<Object> response = webClient.get()
        .uri("/reactive-math/square/{input}/throw", 5)
        .exchangeToMono(this::exchange)
        .doOnError(err -> System.out.println(err.getMessage()));

    StepVerifier.create(response)
        .verifyError(BadRequest.class);
  }

  private Mono<Object> exchange(ClientResponse cr) {
    if(cr.rawStatusCode() == 400) {
      return cr.bodyToMono(InputFailedValidation.class);
    } else {
      return cr.bodyToMono(Response.class);
    }
  }

  @Test
  public void paramsTest() {
    URI uri = UriComponentsBuilder
        .fromUriString("http://localhost:8080/jobs/search?count={count}&page={page}")
        .build(10, 20);

    Flux<Integer> flux = webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(Integer.class)
        .doOnNext(System.out::println);

    StepVerifier.create(flux)
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  public void attributesTest() {
    Mono<Response> response = webClient.post()
        .uri("/reactive-math/multiply")
        .bodyValue(buildReq(2, 3))
        .attribute("auth","basic")
        .retrieve()
        .bodyToMono(Response.class)
        .doOnNext(System.out::println);

    StepVerifier.create(response)
        .expectNextMatches(response1 -> response1.getOutput() == 6)
        .verifyComplete();
  }
}
