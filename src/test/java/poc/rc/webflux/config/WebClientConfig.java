package poc.rc.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .baseUrl("http://localhost:8080")
        .filter((request, next) -> sessionToken(request, next))
//        .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("username", "password"))
        .build();
  }

//  private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
//    System.out.println("--Generate token--");
//    ClientRequest clientRequest = ClientRequest.from(request)
//        .headers(httpHeaders -> httpHeaders.setBearerAuth("some-sample-token"))
//        .build();
//    return ex.exchange(clientRequest);
//  }

  /**
   * when we need different auth for different APIs
   * use webclient attributes
   *
   */
  private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
    ClientRequest clientRequest = request.attribute("auth")
        .map(o -> o.equals("basic") ? withBasicAuth(request) : withOAuth(request))
        .orElse(request);
    return ex.exchange(clientRequest);
  }

  private ClientRequest withBasicAuth(ClientRequest clientRequest) {
    return ClientRequest.from(clientRequest)
        .headers(httpHeaders -> httpHeaders.setBasicAuth("username", "password"))
        .build();
  }

  private ClientRequest withOAuth(ClientRequest clientRequest) {
    return ClientRequest.from(clientRequest)
        .headers(httpHeaders -> httpHeaders.setBearerAuth("some-bearer-token"))
        .build();
  }
}
